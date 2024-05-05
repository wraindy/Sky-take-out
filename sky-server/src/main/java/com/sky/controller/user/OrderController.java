package com.sky.controller.user;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Wraindy
 * @DateTime 2024/05/02 15:45
 * Description
 * Notice
 **/
@RestController("userOrderController")
@Slf4j
@RequestMapping("/user/order")
@Api(tags = "订单相关接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    @ApiOperation("用户下单")
    public Result<OrderSubmitVO> submitOrder(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        log.info("用户下单参数：{}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        // 正式调用微信支付只需要把payment2方法换成payment方法
        OrderPaymentVO orderPaymentVO = orderService.payment2(ordersPaymentDTO);
        log.info("生成预支付交易单（默认微信支付了）：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }

    @GetMapping("/historyOrders")
    @ApiOperation("用户查询自己的历史订单")
    public Result<PageResult> queryHistory(OrdersPageQueryDTO ordersPageQueryDTO){
        log.info("动态查询条件ordersPageQueryDTO：{}",ordersPageQueryDTO);
        PageResult result = orderService.pageQueryOrders(ordersPageQueryDTO);
        log.info("订单查询result：{}",result);
        return Result.success(result);
    }

    @GetMapping("/orderDetail/{id}")
    @ApiOperation("根据订单表主键查询订单详情")
    public Result<OrderVO> queryOrderDetail(@PathVariable Long id){
        OrderVO orderVO = orderService.queryOrderDetail(id);
        return Result.success(orderVO);
    }
}
