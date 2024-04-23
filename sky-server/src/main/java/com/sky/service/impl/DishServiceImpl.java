package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author Wraindy
 * @DateTime 2024/04/23 20:19
 * Description
 * Notice
 **/
@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    /**
     * 新增菜品（带有口味数据）
     * @param dishDTO
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO){

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        // 向菜品表插入一条数据
        dishMapper.insert(dish);

        // xml文件中设置"useGeneratedKeys="true" keyProperty="id""
        // 使得dishMapper.insert(dish)的操作后能返回主键id给dish
        Long dishId = dish.getId();
        List<DishFlavor> flavorList = dishDTO.getFlavors();

        if(flavorList != null && !flavorList.isEmpty()){

            // 每个口味要绑定菜品，一个菜品可以有多个口味
            flavorList.forEach(fl -> fl.setDishId(dishId));

            // 向口味表插入n条数据
            dishFlavorMapper.insertBatch(flavorList);
        }

    }
}
