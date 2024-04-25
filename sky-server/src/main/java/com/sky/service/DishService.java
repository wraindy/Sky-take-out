package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

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

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 菜品批量删除
     * @param ids
     */
    void deleteBatch(List<Long> ids);


    /**
     * 根据菜品id获取菜品信息（带口味信息）
     * @param id
     * @return
     */
    DishVO getByIdWithFlavors(Long id);
}
