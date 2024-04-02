package com.wyq.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wyq.common.CustomException;
import com.wyq.common.R;
import com.wyq.dto.DishDto;
import com.wyq.entity.Category;
import com.wyq.entity.Dish;
import com.wyq.entity.DishFlavor;
import com.wyq.service.CategoryService;
import com.wyq.service.DishFlavorService;
import com.wyq.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;


    /**
     * 新增菜品
     * @param
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info("food:{}",dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    /**
     * 菜品分页
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){

        //新建分页
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        dishService.page(pageInfo,new LambdaQueryWrapper<Dish>()
                .like(name!=null,Dish::getName,name)
                .orderByDesc(Dish::getUpdateTime));

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");


        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item)->{

                DishDto dishDto = new DishDto();
                BeanUtils.copyProperties(item,dishDto);

                Long categoryId = item.getCategoryId();
                Category category = categoryService.getById(categoryId);
                if(category!=null)
                {
                    String categoryName = category.getName();
                    dishDto.setCategoryName(categoryName);
                }
                return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){

        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);

    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){

        dishService.updateWithFlavor(dishDto);

        return R.success("修改菜品成功");
    }

    @PostMapping("/status/{flag}")
    public R<String>  updateStatus(@PathVariable int flag,@RequestParam List<Long> ids)
    {
        //起售
        if(flag == 1)
        {
            int count1 = dishService.count(new LambdaQueryWrapper<Dish>()
                    .in(Dish::getStatus,ids)
                    .eq(Dish::getStatus,1));
            if(count1 > 0)
            {
                throw new CustomException("菜品已经起售");
            }
            dishService.update(new LambdaUpdateWrapper<Dish>()
                    .in(Dish::getId,ids)
                    .set(Dish::getStatus,flag));

        }
        else
        {
            int count0 = dishService.count(new LambdaQueryWrapper<Dish>()
                    .in(Dish::getStatus,ids)
                    .eq(Dish::getStatus,0));
            if(count0 > 0)
            {
                throw new CustomException("菜品已经禁售");
            }
            dishService.update(new LambdaUpdateWrapper<Dish>()
                    .in(Dish::getId,ids)
                    .set(Dish::getStatus,flag));
        }
        return  R.success("修改状态成功");
    }

/*
    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish){

        List<Dish> dishes = dishService.list(new LambdaQueryWrapper<Dish>()
                .eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId())
                .eq(Dish::getStatus,1)
                .orderByAsc(Dish::getSort)
                .orderByDesc(Dish::getUpdateTime));

        return R.success(dishes);
    }
*/

    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){

        List<Dish> dishes = dishService.list(new LambdaQueryWrapper<Dish>()
                .eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId())
                .eq(Dish::getStatus,1)
                .orderByAsc(Dish::getSort)
                .orderByDesc(Dish::getUpdateTime));

        List<DishDto>  dishDtoList = dishes.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category!=null)
            {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            //当前菜品的ID
            Long dishId = item.getId();
            List<DishFlavor> list = dishFlavorService.list(new LambdaQueryWrapper<DishFlavor>()
                                                           .eq(DishFlavor::getDishId,dishId));
            dishDto.setFlavors(list);
            return dishDto;
        }).collect(Collectors.toList());



        return R.success(dishDtoList);
    }



}
