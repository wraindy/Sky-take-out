package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    /**
     * 根据订单号查询订单信息
     * @param outTradeNo
     * @return
     */
    @Select("select * from orders where number = #{outTradeNo}")
    Orders getByNumber(String outTradeNo);

    /**
     * 更新订单信息
     * @param orders
     * id不是订单号，是orders表的自增主键；number才是订单号码
     */
    void update(Orders orders);
}
