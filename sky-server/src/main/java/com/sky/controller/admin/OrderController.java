package com.sky.controller.admin;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author Wraindy
 * @DateTime 2024/05/05 23:15
 * Description
 * Notice
 **/

@RestController("adminOrderController")
@Slf4j
@RequestMapping("/admin/order")
@Api(tags = "订单相关接口")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @GetMapping("/conditionSearch")
    @ApiOperation("动态查询订单搜索")
    public Result<PageResult> queryHistory(OrdersPageQueryDTO ordersPageQueryDTO){
        log.info("动态查询条件ordersPageQueryDTO：{}",ordersPageQueryDTO);
        PageResult result = orderService.queryByCondition(ordersPageQueryDTO);
        return Result.success(result);
    }

    @GetMapping("/details/{id}")
    @ApiOperation("根据订单表主键查询订单详情")
    public Result<OrderVO> queryOrderDetail(@PathVariable Long id){
        OrderVO orderVO = orderService.queryOrderDetail(id);
        return Result.success(orderVO);
    }

    @GetMapping("/statistics")
    @ApiOperation("各个状态的订单数量统计")
    public Result<OrderStatisticsVO> statistics(){
        OrderStatisticsVO orderStatisticsVO =  orderService.getStatistics();
        return Result.success(orderStatisticsVO);
    }

    @PutMapping("/confirm")
    @ApiOperation("接单")
    public Result confirm(@RequestBody Map<String, Long> data){
        // 前端传json{id : xx}
        // 要么构造dto用@RequestBody
        // 要么使用map接收用@RequestBody
        orderService.confirm(data.get("id"));
        return Result.success();
    }
}
