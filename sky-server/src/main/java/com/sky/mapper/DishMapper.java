package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author Wraindy
 * @DateTime 2024/04/20 20:32
 * Description
 * Notice
 **/
@Mapper
public interface DishMapper {
    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(1) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 插入单条菜品
     * @param dish
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据id查询菜品
     * @param id
     */
    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    /**
     * 根据id查询菜品状态
     * @param id
     * @return
     */
    @Select("select status from dish where id = #{id}")
    Integer getStatusById(Long id);

    /**
     * 根据id删除菜品
     * @param id
     */
    @Delete("delete from dish where id = #{id}")
    void deleteById(Long id);

    /**
     * 更新菜品信息
     * @param dish
     */
    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);

    /**
     * 根据菜品id集合批量删除菜品
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据分类id获取菜品集合
     * @param categoryId
     * @return
     */
    @Select("select * from dish where category_id = #{categoryId}")
    List<DishVO> getByCategoryId(Long categoryId);

    /**
     * 根据菜品id集合查询这些菜品的状态集合
     * @param dishIds
     * @return
     */
    List<Integer> getStatusByIds(List<Long> dishIds);

    /**
     * 根据套餐id查询包含的菜品
     * @param setMealId
     * @return
     */
    DishItemVO queryDishItem(Long setMealId);

    /**
     * 根据菜品id获取dishItem所需的image和description
     * @param dishId
     * @return
     */
    @Select("select image, description from dish where id = #{dishId}")
    DishItemVO getDishItem(Long dishId);

    /**
     * 根据不同status计算总数
     * @param status
     * @return
     */
    @Select("select count(id) from dish where status = #{status}")
    Integer countByStatus(Integer status);
}
