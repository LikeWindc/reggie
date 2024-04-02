package com.wyq.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
@Slf4j
/**
 * 默认公共字段的填充
 */
public class MyMetaObjecthandler implements MetaObjectHandler {


    /**
     * 插入操作自动填充
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
    //插入时数据的填充
        log.info("公共字段的自动填充[create]");
        //create_time,create_user,update_time,update_user
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser",new Long(BaseContext.getCurrentId()));
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("createUser",new Long(BaseContext.getCurrentId()));
    }

    /**
     * 更新操作自动填充
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
    //更新时数据的填充
        log.info("公共字段的自动填充[update]");
        log.info(metaObject.toString());
        log.info("当前id为{}",BaseContext.getCurrentId());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser",new Long(BaseContext.getCurrentId()));
        //update_time,update_user
    }
}
