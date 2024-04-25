package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author Wraindy
 * @DateTime 2024/04/24 18:45
 * Description
 * Notice
 **/
@Mapper
public interface SetMealDishMapper {

    /**
     * 根据菜品id查询该菜品被关联的套餐个数
     * 在DishServiceImpl.deleteBatch方法的实现时，不建议把本方法放入for循环中查询，以此代替SetMealDishMapper.getSetMealIdsByDishIds方法
     * 因为网络延迟大于数据库内部查询延迟，应该尽可能减少访问数据库的频率
     * @param id
     * @return
     */
    @Select("select count(*) from setmeal_dish where dish_id = #{id}")
    int checkRelation(Long id);

    /**
     * 根据菜品id，查询套餐id
     * @param dishIds
     * @return
     */
    List<Long> getSetMealIdsByDishIds(List<Long> dishIds);
}
