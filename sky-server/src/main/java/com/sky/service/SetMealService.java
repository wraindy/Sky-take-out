package com.sky.service;

import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;

/**
 * @Author Wraindy
 * @DateTime 2024/04/26 14:36
 * Description
 * Notice
 **/

public interface SetMealService {

    /**
     * 套餐信息分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);
}
