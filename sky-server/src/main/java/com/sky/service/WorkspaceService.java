package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;

import java.time.LocalDateTime;

/**
 * @Author Wraindy
 * @DateTime 2024/05/11 15:17
 * Description
 * Notice
 **/

public interface WorkspaceService {
    /**
     * 查询今日运营数据
     * @return
     */
    BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end);

    /**
     * 查询套餐总览
     * @return
     */
    SetmealOverViewVO setmealOverView();

    /**
     * 查询菜品总览
     * @return
     */
    DishOverViewVO dishOverView();

    /**
     * 查询订单管理数据
     * @return
     */
    OrderOverViewVO orderOverView();
}
