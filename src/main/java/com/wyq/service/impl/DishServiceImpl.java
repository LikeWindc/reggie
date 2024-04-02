package com.wyq.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wyq.dto.DishDto;
import com.wyq.entity.DishFlavor;
import com.wyq.entity.Dish;
import com.wyq.mapper.DishMapper;
import com.wyq.service.DishFlavorService;
import com.wyq.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 存储菜品信息，需要同时处理两张表
     * @param dishDto
     */
    public void saveWithFlavor(DishDto dishDto) {

        this.save(dishDto);

        Long dishId = dishDto.getId();

        List<DishFlavor>  Flavors = dishDto.getFlavors();

        Flavors = Flavors.stream().map((item)->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(Flavors);
    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {

        Dish dish = this.getById(id);

        List<DishFlavor> flavors = dishFlavorService.list(new LambdaQueryWrapper<DishFlavor>()
                .eq(DishFlavor::getDishId,dish.getId()));

        DishDto dishDto = new DishDto();

        BeanUtils.copyProperties(dish,dishDto);

        dishDto.setFlavors(flavors);
        return dishDto;
    }

    /**
     * 修改后更新菜品信息
     * @param dishDto
     */
    @Override
    public void updateWithFlavor(DishDto dishDto) {

        this.updateById(dishDto);
        Long dishId = dishDto.getId();

        List<DishFlavor> flavors = dishDto.getFlavors();

        dishFlavorService.updateBatchById(flavors);

    }
}
