package com.wyq.dto;

import com.wyq.entity.DishFlavor;
import com.wyq.entity.Dish;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;

}
