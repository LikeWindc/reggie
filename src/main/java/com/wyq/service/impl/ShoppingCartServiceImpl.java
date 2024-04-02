package com.wyq.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wyq.entity.ShoppingCart;
import com.wyq.mapper.ShoppingMapper;
import com.wyq.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingMapper, ShoppingCart> implements ShoppingCartService {
}
