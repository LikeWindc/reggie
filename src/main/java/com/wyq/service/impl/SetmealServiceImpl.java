package com.wyq.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wyq.common.CustomException;
import com.wyq.dto.SetmealDto;
import com.wyq.entity.SetmealDish;
import com.wyq.entity.Setmeal;
import com.wyq.mapper.SetmealMapper;
import com.wyq.service.SetmealDishService;
import com.wyq.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Transactional
    public void saveByDish(SetmealDto setmealDto){

        //保存setmeal基本信息
        this.save(setmealDto);

        log.info("setmealDto = {}",setmealDto.toString());

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishes.stream().map((item)->{

            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());


        setmealDishService.saveBatch(setmealDishes);

    }

    /**删除套餐
     *
     */
    @Transactional
    public void removeByDish(List<Long> ids) {

        int count = this.count(new LambdaQueryWrapper<Setmeal>()
                .in(Setmeal::getId,ids)
                .eq(Setmeal::getStatus,1));
        if(count > 0)
        {
            //不能删除则抛出异常
            throw new CustomException("套餐正在售卖中，不能删除");
        }

        this.removeByIds(ids);

        //删除关系表中的数据--setmeal_dish
        setmealDishService.remove(new LambdaQueryWrapper<SetmealDish>()
                .in(SetmealDish::getDishId,ids));
    }

    /**
     * 获取套餐的全部信息
     * @param id
     */
    public SetmealDto getByIdWithDishes(Long id) {

        //获取Setmeal的基本信息
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(this.getById(id),setmealDto);

        //获取套餐的菜品信息
        List<SetmealDish> setmealDishes = setmealDishService
                .list(new LambdaQueryWrapper<SetmealDish>()
                .eq(SetmealDish::getSetmealId,id)
                .orderByAsc(SetmealDish::getSort));

        setmealDto.setSetmealDishes(setmealDishes);

        return setmealDto;

    }

    /**
     * 修改套餐信息
     * @param setmealDto
     */
    public void updateByDish(SetmealDto setmealDto) {
        //更新setmeal基本信息
        this.updateById(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();


        setmealDishes.stream().map((item)->{

            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());


        setmealDishService.remove(new LambdaQueryWrapper<SetmealDish>()
            .eq(SetmealDish::getSetmealId,setmealDto.getId()));

        setmealDishService.saveBatch(setmealDishes);
    }
}
