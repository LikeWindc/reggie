package com.wyq.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wyq.dto.DishDto;
import com.wyq.entity.Dish;

public interface DishService  extends IService<Dish> {

    /**
     * 存储菜品信息，需要同时处理两张表
     */
    public void saveWithFlavor(DishDto dishDto);

    /**
     * 查询菜品信息
     */
    public DishDto getByIdWithFlavor(Long id);

    void updateWithFlavor(DishDto dishDto);

}
