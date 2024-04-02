package com.wyq.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wyq.entity.User;
import com.wyq.mapper.UserMapper;
import com.wyq.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
