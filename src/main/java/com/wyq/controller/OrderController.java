package com.wyq.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.org.apache.xpath.internal.operations.Or;
import com.wyq.common.BaseContext;
import com.wyq.common.R;
import com.wyq.entity.Orders;
import com.wyq.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrdersService ordersService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){

        ordersService.sumbit(orders);
        return R.success("下单成功");
    }

    @GetMapping("/userPage")
    public R<Page> page(@RequestParam int page, int pageSize){
        Page pageInfo = new Page(page,pageSize);

        ordersService.page(pageInfo,new LambdaQueryWrapper<Orders>()
                          .like(Orders::getUserId, BaseContext.getCurrentId()));

        return R.success(pageInfo);

    }

}
