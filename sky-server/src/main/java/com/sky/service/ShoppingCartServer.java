package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

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

    /**
     * 用户查询购物车信息
     * @return
     */
    List<ShoppingCart> queryCart();
}
