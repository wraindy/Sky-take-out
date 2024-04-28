package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;

import java.util.List;

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

    /**
     * 根据id查询套餐信息
     * @param id
     * @return
     */
    SetmealVO getById(Long id);

    /**
     * 修改套餐信息（带有菜品数据）
     * @param setmealDTO
     */
    void updateWithDishes(SetmealDTO setmealDTO);

    /**
     * 套餐的启售和停售
     * @param id
     * @param status
     */
    void startOrStop(Long id, Integer status);

    /**
     * 套餐批量删除
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 新增套餐（带有菜品数据）
     * @param setmealDTO
     */
    void saveWithDishes(SetmealDTO setmealDTO);

    /**
     * 根据分类id查询套餐
     * @param categoryId
     * @return
     */
    List<DishVO> getByCategoryId(Long categoryId);
}
