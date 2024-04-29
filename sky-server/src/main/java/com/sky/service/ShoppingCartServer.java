package com.sky.service;

import com.sky.dto.ShoppingCartDTO;

/**
 * @Author Wraindy
 * @DateTime 2024/04/28 22:07
 * Description
 * Notice
 **/

public interface ShoppingCartServer {
    /**
     * 添加购物车信息
     * @param shoppingCartDTO
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
