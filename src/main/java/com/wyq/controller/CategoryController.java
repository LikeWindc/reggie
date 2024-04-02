package com.wyq.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wyq.common.R;
import com.wyq.entity.Category;
import com.wyq.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 分类管理
 */
@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @PostMapping
    public R<String>  save(@RequestBody Category category){

        log.info("category:{}",category);
        categoryService.save(category);
        return  R.success("新增分类成功");
    }

    @GetMapping("/page")
    public  R<Page> page(int page,int pageSize){

        //分页构造器
        Page<Category> pageInfo = new Page<>(page,pageSize);
        categoryService.page(pageInfo,new LambdaQueryWrapper<Category>().orderByAsc(Category::getSort));

        return R.success(pageInfo);
    }


    @DeleteMapping
    public R<String> delete(Long id){
        log.info("删除分类成功，id为{}",id);
        //categoryService.removeById(id);
        categoryService.remove(id);
        return R.success("分类信息删除成功");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类信息{}" ,category.toString());

        categoryService.updateById(category);

        return R.success("修改分类信息成功");
    }

    /**
     * 根据条件查询分类
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        //条件查询
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();

        //添加条件
        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());

        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list  = categoryService.list(queryWrapper);

        return R.success(list);
    }

}
