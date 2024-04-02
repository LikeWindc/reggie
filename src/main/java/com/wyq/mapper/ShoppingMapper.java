package com.wyq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wyq.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ShoppingMapper extends BaseMapper<ShoppingCart> {
}
