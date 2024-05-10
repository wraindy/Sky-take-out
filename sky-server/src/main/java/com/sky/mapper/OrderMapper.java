package com.sky.mapper;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

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

    /**
     * 条件分页查询
     * @param ordersPageQueryDTO
     * @return
     */
    List<Orders> queryByCondition(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据订单表主键查询订单详情
     * @param id
     * @return
     */
    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);

    /**
     * 各个状态的订单数量统计
     * @return
     */
    @Select("select status from orders")
    List<Integer> getStatistics();

    /**
     * 商家接单
     * @param id
     */
    @Update("update orders set status = 3 where id = #{id}")
    void confirm(Long id);

    /**
     * 根据主键获取订单id
     * @param id
     * @return
     */
    @Select("select orders.number from orders where id = #{id}")
    Long getNumberById(Long id);

    /**
     * 定时取消超时未支付订单
     * @return
     */
    @Update("update orders set status = 6, cancel_time = now(), cancel_reason = '<用户超时未支付，自动取消订单>' where status = 1 and order_time < #{time}")
    Integer autoCancelTimeoutOrder(LocalDateTime time);

    /**
     * 定时完成订单
     * @param time
     * @return
     */
    @Update("update orders set status = 5 where status = 4 and order_time < #{time}")
    Integer autoFinishOrder(LocalDateTime time);
}
