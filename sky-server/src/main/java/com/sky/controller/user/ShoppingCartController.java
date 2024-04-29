package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author Wraindy
 * @DateTime 2024/04/28 22:04
 * Description
 * Notice
 **/
@RestController
@Slf4j
@Api(tags = "购物车相关接口")
@RequestMapping("/user/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartServer shoppingCartServer;

    @PostMapping("/add")
    @ApiOperation("添加购物车商品")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO){
        shoppingCartServer.addShoppingCart(shoppingCartDTO);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("查询购物车信息")
    public Result<List<ShoppingCart>> queryCart(){
        List<ShoppingCart> carts = shoppingCartServer.queryCart();
        return Result.success(carts);
    }
}
