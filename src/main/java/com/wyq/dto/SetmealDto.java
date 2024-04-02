package com.wyq.dto;


import com.wyq.entity.SetmealDish;
import com.wyq.entity.Setmeal;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
