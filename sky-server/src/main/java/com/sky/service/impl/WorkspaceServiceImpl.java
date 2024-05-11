package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.entity.Orders;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Wraindy
 * @DateTime 2024/05/11 15:17
 * Description
 * Notice
 **/
@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private SetMealMapper setMealMapper;

    @Autowired
    private DishMapper dishMapper;

    /**
     * 查询今日运营数据
     * @return
     */
    @Override
    public BusinessDataVO getBusinessData() {

        LocalDateTime begin = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        Map<String, Object> map = new HashMap<>();
        map.put("beginTime", begin);
        map.put("endTime", end);
        map.put("status", Orders.COMPLETED);

        // 今日营业额
        Double turnover = orderMapper.sumByMap(map);

        // 今日新增用户数
        Integer userCount = userMapper.getUserCount(begin, end);

        // 有效订单数
        Integer orderCount = orderMapper.getOrderCountByMap(map);

        // 平均客单价
        Double average = turnover / orderCount;

        // 订单完成率
        map.put("status", null);
        Integer totalOrderCount = orderMapper.getOrderCountByMap(map);
        Double rate = orderCount / totalOrderCount.doubleValue();

        return BusinessDataVO
                .builder()
                .turnover(turnover)
                .newUsers(userCount)
                .unitPrice(average)
                .orderCompletionRate(rate)
                .validOrderCount(orderCount)
                .build();
    }

    /**
     * 查询套餐总览
     * @return
     */
    @Override
    public SetmealOverViewVO setmealOverView() {
        Integer sold = setMealMapper.countByStatus(StatusConstant.ENABLE);
        Integer discontinued = setMealMapper.countByStatus(StatusConstant.DISABLE);
        return new SetmealOverViewVO(sold, discontinued);
    }

    /**
     * 查询菜品总览
     * @return
     */
    @Override
    public DishOverViewVO dishOverView() {
        Integer sold = dishMapper.countByStatus(StatusConstant.ENABLE);
        Integer discontinued = dishMapper.countByStatus(StatusConstant.DISABLE);
        return new DishOverViewVO(sold, discontinued);
    }
}
