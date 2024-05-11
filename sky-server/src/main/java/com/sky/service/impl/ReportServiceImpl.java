package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Wraindy
 * @DateTime 2024/05/10 21:11
 * Description
 * Notice
 **/
@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 营业额统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {

        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while(!begin.equals(end)){
            // 将日期范围内的每一天放入list
            // 注意：LocalDate不可变，需要重新赋值
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        String dateStr = StringUtils.join(dateList, ",");

        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate day : dateList){
            LocalDateTime beginTime = LocalDateTime.of(day, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(day, LocalTime.MAX);

            Map<String, Object> map = new HashMap<>();
            map.put("beginTime", beginTime);
            map.put("endTime", endTime);
            map.put("status", Orders.COMPLETED);

            Double turnover = orderMapper.sumByMap(map);
            turnoverList.add(turnover == null ? 0.0 : turnover);
        }

        String turnoverStr = StringUtils.join(turnoverList,",");

        return TurnoverReportVO
                .builder()
                .dateList(dateStr)
                .turnoverList(turnoverStr)
                .build();
    }

    /**
     * 用户统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {

        List<LocalDate> dataList = new ArrayList<>();
        dataList.add(begin);
        while(!begin.equals(end)){
            begin = begin.plusDays(1);
            dataList.add(begin);
        }
        String dataStr = StringUtils.join(dataList, ",");

        List<Integer> totalUserList = new ArrayList<>();
        List<Integer> newUserList = new ArrayList<>();
        for (LocalDate day : dataList){
            LocalDateTime today = LocalDateTime.of(day, LocalTime.MAX);
            Integer total = userMapper.getUserCount(null, today);
            totalUserList.add(total);

            LocalDateTime beginTime = LocalDateTime.of(day, LocalTime.MIN);
            Integer sum = userMapper.getUserCount(beginTime, today);
            newUserList.add(sum);
        }
        String totalUserStr = StringUtils.join(totalUserList, ",");
        String newUserStr = StringUtils.join(newUserList, ",");

        return UserReportVO
                .builder()
                .dateList(dataStr)
                .totalUserList(totalUserStr)
                .newUserList(newUserStr)
                .build();
    }
}
