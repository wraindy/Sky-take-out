package com.sky.mapper;

import com.sky.entity.SetmealDish;
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
     * @param dishId
     * @return
     */
    @Select("select count(*) from setmeal_dish where dish_id = #{dishId}")
    int checkRelation(Long dishId);

    /**
     * 根据菜品id，查询套餐id
     * @param dishIds
     * @return
     */
    List<Long> getSetMealIdsByDishIds(List<Long> dishIds);

    /**
     * 根据套餐id获取菜品信息
     * @param setmealId
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_dish.setmeal_id = #{setmealId}")
    List<SetmealDish> getBySetmealId(Long setmealId);

    /**
     * 根据套餐id删除与菜品的关联
     * @param id
     */
    void deleteBySetMealId(Long id);

    /**
     * 插入新的套餐与菜品的关联
     * @param setmealDishes
     */
    void insert(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐id获取该套餐包含的菜品id集合
     * @param setMealId
     * @return
     */
    @Select("select dish_id from setmeal_dish where setmeal_id = #{setMealId}")
    List<Long> getDishIdsBySetMealId(Long setMealId);

    /**
     * 根据传入的套餐id结合，删除套餐与菜品的关联数据
     * @param ids
     */
    void deleteBySetMealIds(List<Long> ids);

    /**
     * 根据套餐id获取dishItem所需的name和copies信息
     * @param setMealId
     * @return
     */
    @Select("select dish_id, name, copies from setmeal_dish where setmeal_id = #{setMealId}")
    List<SetmealDish> getDishItem(Long setMealId);
}
