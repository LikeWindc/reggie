package com.wyq.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.wyq.common.BaseContext;
import com.wyq.common.R;
import com.wyq.entity.AddressBook;
import com.wyq.service.AdressBookService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/addressBook")
@Slf4j
public class AdressBookController {

    @Autowired
    private  AdressBookService adressBookService;


    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook){

        List<AddressBook> adds =  adressBookService.list();

        return R.success(adds);
    }

    @PostMapping
    public R<String> save(@RequestBody AddressBook addressBook , HttpSession session) {

        addressBook.setUserId(BaseContext.getCurrentId());
        adressBookService.save(addressBook);

        return R.success("新增地址成功");
    }

    @PutMapping("/default")
    public R<String> def(@RequestBody AddressBook addressBook)
    {

        adressBookService.update(new LambdaUpdateWrapper<AddressBook>()
                         .eq(AddressBook::getIsDefault,1)
                         .set(AddressBook::getIsDefault,0));


        adressBookService.update(new LambdaUpdateWrapper<AddressBook>()
                                .eq(AddressBook::getId,addressBook.getId())
                                .set(AddressBook::getIsDefault,1));
        return R.success("设为默认地址成功");
    }

    @GetMapping("/{id}")
    public R<AddressBook> get(@PathVariable Long id){

        AddressBook addressBook = new AddressBook();

        addressBook = adressBookService.getById(id);

        return R.success(addressBook);

    }

    @PutMapping
    public R<String> update(@RequestBody AddressBook addressBook)
    {



        adressBookService.updateById(addressBook);
        return R.success("地址修改成功");

    }

    @GetMapping("/default")
    public R<AddressBook> getDef(){

        AddressBook addressBook = adressBookService.getOne(new LambdaQueryWrapper<AddressBook>()
                                                           .eq(AddressBook::getUserId,BaseContext.getCurrentId())
                                                           .eq(AddressBook::getIsDefault,1));

        return R.success(addressBook);
    }

}
