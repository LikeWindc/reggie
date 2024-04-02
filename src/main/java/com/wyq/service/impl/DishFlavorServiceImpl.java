package com.wyq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wyq.entity.DishFlavor;
import com.wyq.mapper.DishFlavorMapper;
import com.wyq.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
