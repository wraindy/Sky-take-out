package com.sky.service;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
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
}
