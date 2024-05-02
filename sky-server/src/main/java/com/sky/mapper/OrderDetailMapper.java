package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author Wraindy
 * @DateTime 2024/05/02 20:31
 * Description
 * Notice
 **/
@Mapper
public interface OrderDetailMapper {
    /**
     * 批量插入订单详情数据
     * @param odList
     */
    void insertBatch(List<OrderDetail> odList);
}
