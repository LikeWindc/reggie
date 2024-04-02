package com.wyq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wyq.dto.SetmealDto;
import com.wyq.entity.Setmeal;

import java.util.List;


public interface SetmealService extends IService<Setmeal> {

    public void saveByDish(SetmealDto setmealDto);


    /**删除套餐
     *
     */
    public void removeByDish(List<Long> ids);

    /**
     * 获取套餐的全部信息
     * @param id
     */
    public SetmealDto getByIdWithDishes(Long id);

    /**
     * 修改套餐信息
     * @param setmealDto
     */
    public void updateByDish(SetmealDto setmealDto);
}
