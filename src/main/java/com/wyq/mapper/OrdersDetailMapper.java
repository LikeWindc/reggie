package com.wyq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wyq.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersDetailMapper extends BaseMapper<OrderDetail> {
}
