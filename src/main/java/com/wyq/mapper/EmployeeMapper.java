package com.wyq.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wyq.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper  extends BaseMapper <Employee>{


}
