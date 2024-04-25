package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author Wraindy
 * @DateTime 2024/04/23 20:19
 * Description
 * Notice
 **/
@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    /**
     * 新增菜品（带有口味数据）
     * @param dishDTO
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO){

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        // 向菜品表插入一条数据
        dishMapper.insert(dish);

        // xml文件中设置"useGeneratedKeys="true" keyProperty="id""
        // 使得dishMapper.insert(dish)的操作后能返回主键id给dish
        Long dishId = dish.getId();
        List<DishFlavor> flavorList = dishDTO.getFlavors();

        if(flavorList != null && !flavorList.isEmpty()){

            // 每个口味要绑定菜品，一个菜品可以有多个口味
            flavorList.forEach(fl -> fl.setDishId(dishId));

            // 向口味表插入n条数据
            dishFlavorMapper.insertBatch(flavorList);
        }

    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {

        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 批量删除菜品
     * @param ids
     */
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {

        // 判断菜品是否能够删除
        for(Long id : ids){
            // 判断菜品是否起售
            // 下面if语句没有跟黑马，而是自己写的getStatusById
            if(Objects.equals(dishMapper.getStatusById(id), StatusConstant.ENABLE)){
                // 菜品在起售，不能删除
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

          // 以下是黑马原版
//        for(Long id : ids){
//            Dish dish = dishMapper.getById(id);
//            if(Objects.equals(dish.getStatus(), StatusConstant.ENABLE)){
//                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
//            }
//        }

        // 判断菜品是否被套餐关联
        List<Long> setMealIds = setMealDishMapper.getSetMealIdsByDishIds(ids);
        if (setMealIds != null && !setMealIds.isEmpty()){
            // 当前菜品集合存在个别菜品被套餐关联了，不能删除
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        for(Long id : ids){
            // 删除菜品表数据
            dishMapper.deleteById(id);
            // 删除菜品对应的口味表数据
            dishFlavorMapper.deleteByDishId(id);
        }
    }

}