package com.wyq.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import com.wyq.common.R;
import com.wyq.entity.Employee;
import com.wyq.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;


@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        //对密码进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        //查找数据库是否存在账号信息
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp =  employeeService.getOne(queryWrapper);
        //没有查询到返回失败结果
        if(emp == null){
            return R.error("没有用户信息！");
        }
        //对照密码是否正确
        if(!emp.getPassword().equals(password)){
            return R.error(("密码错误！"));
        }
        //查看员工是否被禁用
        if(emp.getStatus() == 0){
            return R.error("账号已经禁用!");

        }
        //登陆成功，将员工id存入Session返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());

        return R.success(emp);
    }


    /**
     * 员工退出
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }



    @PostMapping
    public R<String> Save(HttpServletRequest request, @RequestBody Employee employee){
        log.info("新加员工，员工信息：{}",employee.toString());

        //设置员工初始密码
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8)));
        //employee.setCreateTime(LocalDateTime.now());
        //employee.setUpdateTime(LocalDateTime.now());
        //获取当前登录用户的id
        Long empId = (Long) request.getSession().getAttribute("employee");

        //employee.setCreateUser(empId);
        //employee.setUpdateUser(empId);
        employeeService.save(employee);


        return R.success("新增员工成功");
    }

    /**
     * 员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        log.info("page = {},pageSize = {}, name = {}",page,pageSize,name);

        //构造分页构造器
        Page pageInfo = new Page(page,pageSize);

        employeeService.page(pageInfo,new LambdaQueryWrapper<Employee>()
                .orderByDesc(Employee::getUpdateTime)
                .like(StringUtils.isNotEmpty(name),Employee::getName,name));
        return R.success(pageInfo);
    }

    @PutMapping
    public R<String> update(@RequestBody Employee employee,HttpServletRequest request){
        log.info(employee.toString());

        //employee.setUpdateTime(LocalDateTime.now());
        //employee.setUpdateUser(id);

        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工信息");
        Employee employee = employeeService.getById(id);

        if(employee != null)
            return R.success(employee);
        else
            return R.error("没有查询到员工信息");
    }
}
