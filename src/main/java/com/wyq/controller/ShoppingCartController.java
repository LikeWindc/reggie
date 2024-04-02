package com.wyq.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.wyq.common.BaseContext;
import com.wyq.common.R;
import com.wyq.entity.ShoppingCart;
import com.wyq.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController  {

    @Autowired
    private ShoppingCartService shoppingCartService;


    @PostMapping("/add")
    public R<ShoppingCart>  save(@RequestBody ShoppingCart shoppingCart)
    {
        shoppingCart.setUserId(BaseContext.getCurrentId());
        //判断是否在购物车中


        ShoppingCart newShoppingCart =
                shoppingCartService.getOne(new LambdaUpdateWrapper<ShoppingCart>()
                                   .eq(ShoppingCart::getUserId,BaseContext.getCurrentId())
                                   .eq(ShoppingCart::getDishId,shoppingCart.getDishId())
                                   .or()
                                   .eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId()));


        //商品在购物车中
        if(newShoppingCart != null)
        {
            newShoppingCart.setNumber(newShoppingCart.getNumber()+1);
            shoppingCartService.updateById(newShoppingCart);
        }else{
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
        }

        return R.success(shoppingCart);
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){

        List<ShoppingCart> shoppingCarts = shoppingCartService.list(new LambdaUpdateWrapper<ShoppingCart>()
                                                                    .eq(ShoppingCart::getUserId,BaseContext.getCurrentId())
                                                                    .orderByDesc(ShoppingCart::getCreateTime));

        return R.success(shoppingCarts);
    }


    @DeleteMapping("/clean")
    public R<String> delete()
    {
        shoppingCartService.remove(new LambdaQueryWrapper<ShoppingCart>()
                                   .eq(ShoppingCart::getUserId,BaseContext.getCurrentId()));

        return R.success("删除成功");
    }


    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){

        ShoppingCart shoppingCartNew =   shoppingCartService.getOne(new LambdaUpdateWrapper<ShoppingCart>()
                .eq(ShoppingCart::getUserId,BaseContext.getCurrentId())
                .eq(ShoppingCart::getDishId,shoppingCart.getDishId()));



        if(shoppingCartNew.getNumber()!=1)
        {
            shoppingCartNew.setNumber(shoppingCartNew.getNumber()-1);
            shoppingCartService.updateById(shoppingCartNew);
        }else{
            shoppingCartService.removeById(shoppingCartNew);
        }

        return R.success(shoppingCart);

    }
}
