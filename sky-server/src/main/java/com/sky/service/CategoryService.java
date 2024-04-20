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
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);
}
