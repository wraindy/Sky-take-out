package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Author Wraindy
 * @DateTime 2024/04/20 20:32
 * Description
 * Notice
 **/
@Mapper
public interface SetMealMapper {
    /**
     * 根据分类id查询套餐数量
     * @param categoryId
     * @return
     */
    @Select("select count(1) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 分页查询套餐信息
     * @param setmealPageQueryDTO
     * @return
     */
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);
}
