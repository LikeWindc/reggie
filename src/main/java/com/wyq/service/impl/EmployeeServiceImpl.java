package com.wyq.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wyq.entity.Employee;
import com.wyq.mapper.EmployeeMapper;
import com.wyq.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl  <EmployeeMapper, Employee> implements EmployeeService {


}
