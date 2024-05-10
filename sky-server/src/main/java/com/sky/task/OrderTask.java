package com.sky.task;

import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Author Wraindy
 * @DateTime 2024/05/10 10:37
 * Description
 * Notice
 **/
@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理超时未支付订单
     */
    @Scheduled(cron = "0 * * * * ?")
    public void processTimeoutOrder(){
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        Integer result = orderMapper.autoCancelTimeoutOrder(time);
        log.info("定时处理超时未支付订单：{}", (result > 0? " <已取消" + result + "个订单> " : " <暂无超时订单> "));
    }

    /**
     * 处理一直处于<派送中4>状态的订单（前一天的）
     * 每天凌晨1点执行一次
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void processUnfinishedOrder(){
        LocalDateTime time = LocalDateTime.now().plusHours(-1);
        Integer result = orderMapper.autoFinishOrder(time);
        log.info("定时处理<派送中>的订单：{}", (result > 0? " <已完成" + result + "个订单> " : " <暂无派送中订单> "));
    }
}
