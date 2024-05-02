package com.sky.service;

import com.sky.dto.OrdersSubmitDTO;
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
}
