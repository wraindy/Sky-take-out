package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author Wraindy
 * @DateTime 2024/04/26 14:38
 * Description
 * Notice
 **/
@Service
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealMapper setMealMapper;

    @Autowired
    private SetMealDishMapper setMealDishMapper;

    /**
     * 套餐信息分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setMealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 根据id查询套餐信息
     * @param id
     * @return
     */
    @Override
    public SetmealVO getById(Long id) {
        Setmeal setmeal = setMealMapper.getById(id);
        List<SetmealDish> setmealDishes = setMealDishMapper.getBySetmealId(id);
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    /**
     * 修改套餐信息（带有菜品数据）
     * @param setmealDTO
     */
    @Override
    @Transactional
    public void updateWithDishes(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setMealMapper.update(setmeal);
        setMealDishMapper.deleteBySetMealId(setmealDTO.getId());
        for(SetmealDish sd : setmealDTO.getSetmealDishes()){
            sd.setSetmealId(setmealDTO.getId());
        }
        setMealDishMapper.insert(setmealDTO.getSetmealDishes());
    }

    /**
     * 套餐的启售和停售
     * @param id
     * @param status
     */
    @Override
    public void startOrStop(Long id, Integer status) {
        Setmeal setmeal = Setmeal
                .builder()
                .id(id)
                .status(status)
                .build();
        setMealMapper.update(setmeal);
    }
}
