package com.sky.service;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;

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
}
