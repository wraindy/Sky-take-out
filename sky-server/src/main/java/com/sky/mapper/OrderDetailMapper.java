package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    /**
     * 根据订单号批量查询订单详情
     * @param number
     * @return
     */
    @Select("select * from order_detail where order_id = #{number}")
    List<OrderDetail> getByNumber(String number);

    /**
     * 查询构造dishString必要的字段
     * @param number
     * @return
     */
    @Select("select name, number from order_detail where order_id = #{number}")
    List<OrderDetail> getDishStringItem(String number);
}
