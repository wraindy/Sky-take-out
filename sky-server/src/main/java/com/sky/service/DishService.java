package com.sky.service;

import com.sky.dto.DishDTO;

/**
 * @Author Wraindy
 * @DateTime 2024/04/23 20:19
 * Description
 * Notice
 **/

public interface DishService {

    /**
     * 新增菜品（带有口味数据）
     * @param dishDTO
     */
    void saveWithFlavor(DishDTO dishDTO);
}
