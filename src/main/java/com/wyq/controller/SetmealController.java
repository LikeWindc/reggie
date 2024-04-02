package com.wyq.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wyq.dto.SetmealDto;
import com.wyq.common.CustomException;
import com.wyq.common.R;
import com.wyq.entity.Setmeal;
import com.wyq.service.CategoryService;
import com.wyq.service.SetmealDishService;
import com.wyq.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;


    /**
     * 保存新增套餐
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){

        log.info("套餐信息:{}",setmealDto.toString());
        setmealService.saveByDish(setmealDto);

        return R.success("保存成功");
    }

    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){

        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> DtoPage = new Page<>(page,pageSize);

        setmealService.page(pageInfo,new LambdaQueryWrapper<Setmeal>()
                .like(name != null,Setmeal::getName,name)
                .orderByDesc(Setmeal::getUpdateTime));

       BeanUtils.copyProperties(pageInfo,DtoPage,"records");

        List<Setmeal>  records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item)->{
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            String cateName = categoryService.getById(item.getCategoryId()).getName();
            if(cateName != null){

                setmealDto.setCategoryName(cateName);
            }
            return setmealDto;
        }).collect(Collectors.toList());


        DtoPage.setRecords(list);
        return R.success(DtoPage);

    }

    @GetMapping("/{id}")
    public R<SetmealDto> get(@PathVariable Long id){

        SetmealDto setmealDto = setmealService.getByIdWithDishes(id);

        return R.success(setmealDto);

    }


    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){

        setmealService.removeByDish(ids);

        return R.success("删除成功");
    }


    /**
     * 修改套餐信息
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){


         setmealService.updateByDish(setmealDto);

         return R.success("套餐信息修改成功");
    }


    @PostMapping("/status/{flag}")
    public R<String>  updateStatus(@PathVariable int flag,@RequestParam List<Long> ids)
    {
        //起售
        if(flag == 1)
        {
            int count1 = setmealService.count(new LambdaQueryWrapper<Setmeal>()
                    .in(Setmeal::getStatus,ids)
                    .eq(Setmeal::getStatus,1));
            if(count1 > 0)
            {
                throw new CustomException("菜品已经起售");
            }
            setmealService.update(new LambdaUpdateWrapper<Setmeal>()
                    .in(Setmeal::getId,ids)
                    .set(Setmeal::getStatus,flag));

        }
        else
        {
            int count0 = setmealService.count(new LambdaQueryWrapper<Setmeal>()
                    .in(Setmeal::getStatus,ids)
                    .eq(Setmeal::getStatus,0));
            if(count0 > 0)
            {
                throw new CustomException("菜品已经禁售");
            }
            setmealService.update(new LambdaUpdateWrapper<Setmeal>()
                    .in(Setmeal::getId,ids)
                    .set(Setmeal::getStatus,flag));
        }
        return  R.success("修改状态成功");
    }

    @GetMapping("/list")
    public  R<List<Setmeal>> list(@RequestParam Long categoryId,int status)
    {
            List<Setmeal> setmeals =
            setmealService.list(new LambdaQueryWrapper<Setmeal>()
                                .eq(Setmeal::getCategoryId,categoryId)
                                .eq(Setmeal::getStatus,status)
                                .orderByDesc(Setmeal::getUpdateTime));

            return R.success(setmeals);
    }


}
