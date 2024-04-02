package com.wyq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wyq.common.CustomException;
import com.wyq.entity.Category;
import com.wyq.entity.Dish;
import com.wyq.entity.Setmeal;
import com.wyq.mapper.CategoryMapper;
import com.wyq.service.CategoryService;
import com.wyq.service.DishService;
import com.wyq.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类
     * @param id
     */
    @Override
    public void remove(Long id) {

        //判断分类相关是否有菜品
        int countDish = dishService.count(new LambdaQueryWrapper<Dish>().eq(Dish::getCategoryId,id));

        //如果相关有菜品，抛出业务异常
        if(countDish > 0){

            throw new CustomException("当前分类关联了菜品，不能删除");
        }

        //判断分类相关是否有套餐
        int countSetmeal = setmealService.count(new LambdaQueryWrapper<Setmeal>().eq(Setmeal::getCategoryId,id));
        //如果相关有菜品，抛出业务异常
        if(countSetmeal > 0){
            throw new CustomException("当前分类关联了套餐，不能删除");
        }

        //如果没有关联信息，正常删除
        super.removeById(id);
    }
}
