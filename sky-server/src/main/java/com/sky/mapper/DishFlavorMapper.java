package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author Wraindy
 * @DateTime 2024/04/23 20:36
 * Description
 * Notice
 **/
@Mapper
public interface DishFlavorMapper {
    /**
     * 批量插入口味（多个口味绑定一个菜）
     * @param flavorList
     */
    void insertBatch(List<DishFlavor> flavorList);

    /**
     * 根据菜品id删除对应菜品的口味数据
     * @param dishId
     */
    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deleteByDishId(Long dishId);

    /**
     * 根据菜品id集合批量删除菜品口味数据
     * @param ids
     */
    void deleteBatchByDishIds(List<Long> ids);

    /**
     * 根据菜品id获取口味数据
     * @param dishId
     * @return
     */
    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> getByDishId(Long dishId);
}
