package com.wyq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wyq.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
