package com.wyq.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wyq.entity.OrderDetail;
import com.wyq.mapper.OrdersDetailMapper;
import com.wyq.service.OrdersDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrdersDetailServiceImpl extends ServiceImpl<OrdersDetailMapper, OrderDetail> implements OrdersDetailService {
}
