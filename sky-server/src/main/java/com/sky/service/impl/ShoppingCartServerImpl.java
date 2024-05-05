package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartServer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author Wraindy
 * @DateTime 2024/04/28 22:07
 * Description
 * Notice
 **/
@Service
public class ShoppingCartServerImpl implements ShoppingCartServer {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetMealMapper setMealMapper;

    /**
     * 添加购物车信息
     * @param shoppingCartDTO
     */
    @Override
    @Transactional
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {

        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        // 如果数据库中存在购物车数据
        if(list != null && !list.isEmpty()){
            // 购物车数据唯一，因此取index = 0
            ShoppingCart sc = list.get(0);
            sc.setNumber(sc.getNumber()+1);
            shoppingCartMapper.updateNumberById(sc);
        } else{

            // 数据库中不存在,则新增购物车信息
            if (shoppingCart.getSetmealId() == null){
                // 当前要新增菜品信息
                // todo 优化不必一次性查询这么多数据
                Dish dish = dishMapper.getById(shoppingCart.getDishId());
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setNumber(1);
                shoppingCart.setCreateTime(LocalDateTime.now());
            } else{
                // 当前要新增套餐信息
                Setmeal setmeal = setMealMapper.getById(shoppingCart.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            // 通用操作移到外部
            // todo 下文插入数据时，没有插入number，虽然数据库有默认值，考虑能否优化
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());

            // 构造好购物车数据就执行insert
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    /**
     * 用户查询购物车信息
     * @return
     */
    @Override
    public List<ShoppingCart> queryCart() {
        Long userId = BaseContext.getCurrentId();
        return shoppingCartMapper.queryCart(userId);
    }

    /**
     * 清空购物车
     */
    @Override
    public void cleanAll() {
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.cleanAllByUserId(userId);
    }

    /**
     * 删除购物车中的一个商品
     * @param shoppingCartDTO
     */
    @Override
    public void subOne(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        ShoppingCart sc = list.get(0);
        if(sc != null && sc.getNumber() > 1){
            sc.setNumber(sc.getNumber() - 1);
            shoppingCartMapper.updateNumberById(sc);
        } else if (sc != null && sc.getNumber() == 1) {
            shoppingCartMapper.removeItem(shoppingCart);
        }
    }
}
