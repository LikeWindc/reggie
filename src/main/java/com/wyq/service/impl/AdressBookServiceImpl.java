package com.wyq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wyq.entity.AddressBook;
import com.wyq.mapper.AdressBookMapper;
import com.wyq.service.AdressBookService;
import org.springframework.stereotype.Service;


@Service
public class AdressBookServiceImpl extends ServiceImpl<AdressBookMapper, AddressBook> implements AdressBookService {
}
