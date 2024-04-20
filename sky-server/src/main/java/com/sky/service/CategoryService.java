package com.sky.service;

import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;

/**
 * @Author Wraindy
 * @DateTime 2024/04/20 20:31
 * Description
 * Notice
 **/
public interface CategoryService {
    /**
     * 分页查询分类列表
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 启用或停用分类
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);
}
