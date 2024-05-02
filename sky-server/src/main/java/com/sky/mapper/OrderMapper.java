package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author Wraindy
 * @DateTime 2024/05/02 16:49
 * Description
 * Notice
 **/
@Mapper
public interface OrderMapper {
    /**
     * 向订单表插入数据
     * @param orders
     */
    void insert(Orders orders);
}
