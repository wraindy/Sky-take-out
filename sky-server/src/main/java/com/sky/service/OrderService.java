package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.*;

/**
 * @Author Wraindy
 * @DateTime 2024/05/02 16:37
 * Description
 * Notice
 **/

public interface OrderService {
    /**
     * 用户下单
     * @param ordersSubmitDTO
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 微信支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * 订单支付（除去微信支付的版本）
     * @param ordersPaymentDTO
     * @return
     * payment2与payment方法相比，直接修改订单数据，
     * 然后删去了后端调用微信支付接口生成预支付交易单的功能，
     * 接着直接向小程序返回调起支付所需的假的参数（https://pay.weixin.qq.com/docs/merchant/apis/mini-program-payment/mini-transfer-payment.html）
     */
    OrderPaymentVO payment2(OrdersPaymentDTO ordersPaymentDTO);

    /**
     * 用户查询自己的历史订单记录
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult queryHistory(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据订单表主键查询订单详情
     * @param id
     * @return
     */
    OrderVO queryOrderDetail(Long id);

    /**
     * 动态查询订单搜索
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult queryByCondition(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 各个状态的订单数量统计
     *
     * @return
     */
    OrderStatisticsVO getStatistics();

    /**
     * 商家接单
     */
    void confirm(Long id);

    /**
     * 商家拒单
     * 只有<待接单2>的状态才能拒单
     * @param ordersRejectionDTO
     */
    void rejection(OrdersRejectionDTO ordersRejectionDTO);

    /**
     * 商家取消订单
     * 只有<待付款1><待派送3><派送中4><已完成5>才能取消订单
     * 注意：<待派送3> === 数据库中orders表status字段的<已接单3>
     * @param ordersCancelDTO
     */
    void cancel(OrdersCancelDTO ordersCancelDTO);

    /**
     * 用户取消订单
     * 只有<待付款1><待接单2>状态才能取消订单
     * @param id
     */
    void userCancel(Long id);

    /**
     * 用户再来一单
     * @param id
     */
    void repetition(Long id);

    /**
     * 商家派单
     * @param id
     */
    void delivery(Long id);

    /**
     * 商家完成订单
     * @param id
     */
    void complete(Long id);
}
