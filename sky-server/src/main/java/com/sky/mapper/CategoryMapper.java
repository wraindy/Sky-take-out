package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author Wraindy
 * @DateTime 2024/04/20 20:30
 * Description
 * Notice
 **/
@Mapper
public interface CategoryMapper {
    /**
     * 分页查询分类列表
     * @param categoryPageQueryDTO
     * @return
     */
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);
}
