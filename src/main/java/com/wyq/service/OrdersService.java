package com.wyq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wyq.entity.Orders;

public interface OrdersService extends IService<Orders> {

    /**
     * 用户提交订单
     * @param orders
     */
    public void sumbit(Orders orders);
}
