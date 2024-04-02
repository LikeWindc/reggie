package com.wyq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wyq.common.BaseContext;
import com.wyq.common.CustomException;
import com.wyq.entity.*;
import com.wyq.mapper.OrdersMapper;
import com.wyq.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper,Orders> implements OrdersService {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserService userService;

    @Autowired
    private AdressBookService adressBookService;

    @Autowired
    private OrdersDetailService ordersDetailService;

    /**
     * 用户提交订单
     * @param orders
     */
    public void sumbit(Orders orders) {

        //获取用户id
        Long userId = BaseContext.getCurrentId();

        //获取用户购物车信息
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(new LambdaQueryWrapper<ShoppingCart>()
                                                            .eq(ShoppingCart::getUserId,userId)
                                                            .orderByDesc(ShoppingCart::getCreateTime));
        if (shoppingCarts == null)
        {

            throw new CustomException("购物车为空！不能下单");
        }
        //获取用户信息
        User user = userService.getById(userId);

        //查询地址数据
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = adressBookService.getById(addressBookId);



        long orderId = IdWorker.getId();


        AtomicInteger amount = new AtomicInteger(0);
        List<OrderDetail> orderDetails = shoppingCarts.stream()
                .map(item -> {
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setOrderId(orderId);
                    orderDetail.setNumber(item.getNumber()); // 修正了方法名的拼写错误
                    orderDetail.setDishFlavor(item.getDishFlavor());
                    orderDetail.setDishId(item.getDishId());
                    orderDetail.setSetmealId(item.getSetmealId());
                    orderDetail.setName(item.getName());
                    orderDetail.setImage(item.getImage()); // 修正了属性名和方法名的拼写错误
                    orderDetail.setAmount(item.getAmount());
                    // 下面这行代码似乎是想累加每个订单项的总价到某个总金额变量中，但是在这段代码中没有提供完整的上下文
                    // 假设存在一个 AtomicInteger 类型的 amount 变量用于累加总金额
                    amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
                    return orderDetail;
                })
                .collect(Collectors.toList());

        orders.setNumber(String.valueOf(orderId));
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));
        orders.setUserId(userId);
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getPhone());
        orders.setAddress(addressBook.getProvinceName() == null? "": addressBook.getProvinceName()
                +addressBook.getCityName() == null?"":addressBook.getCityName()
                +addressBook.getDistrictName() == null?"":addressBook.getDistrictName()
                +addressBook.getDetail()==null?"":addressBook.getDetail());


        //向订单表插入一条数据
        this.save(orders);


        ordersDetailService.saveBatch(orderDetails);

        //删除购物车数据
        shoppingCartService.remove(new LambdaQueryWrapper<ShoppingCart>().eq(ShoppingCart::getUserId,userId));

    }
}
