package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Autowired
    private WorkspaceService workspaceService;

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

    /**
     * 订单统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO ordersStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dataList = new ArrayList<>();
        dataList.add(begin);
        while(!begin.equals(end)){
            begin = begin.plusDays(1);
            dataList.add(begin);
        }
        String dataStr = StringUtils.join(dataList, ",");

        //每天订单总数集合
        List<Integer> orderCountList = new ArrayList<>();
        //每天有效订单数集合
        List<Integer> validOrderCountList = new ArrayList<>();
        for (LocalDate day : dataList){
            LocalDateTime beginTime = LocalDateTime.of(day, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(day, LocalTime.MAX);

            Map<String, Object> map = new HashMap<>();
            map.put("beginTime", beginTime);
            map.put("endTime", endTime);
            map.put("status", null);
            orderCountList.add(orderMapper.getOrderCountByMap(map));
            
            map.put("status", Orders.COMPLETED);
            validOrderCountList.add(orderMapper.getOrderCountByMap(map));
        }
        String orderCountListStr = StringUtils.join(orderCountList, ",");
        String validOrderCountListStr = StringUtils.join(validOrderCountList, ",");

        Integer totalOrderCount = orderCountList.stream().reduce(0, Integer::sum);
        Integer validOrderCount = validOrderCountList.stream().reduce(0, Integer::sum);

        Double rate = 0.0;
        if (totalOrderCount != 0){
            rate = validOrderCount.doubleValue() / totalOrderCount.doubleValue();
        }

        return OrderReportVO
                .builder()
                .dateList(dataStr)
                .orderCompletionRate(rate)
                .orderCountList(orderCountListStr)
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .validOrderCountList(validOrderCountListStr)
                .build();
    }

    /**
     * 销量排名统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        List<GoodsSalesDTO> list = orderMapper.getTop10(beginTime, endTime);

        String nameList = StringUtils.join(list.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList()), ",");
        String numberList = StringUtils.join(list.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList()), ",");

        return SalesTop10ReportVO
                .builder()
                .nameList(nameList)
                .numberList(numberList)
                .build();
    }

    /**
     * 导出运营报表（最近三十天）
     * @param response
     */
    @Override
    public void exportBusinessData(HttpServletResponse response) {

        LocalDateTime begin = LocalDateTime.of(LocalDate.now().minusDays(30), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MAX);
        BusinessDataVO vo = workspaceService.getBusinessData(begin, end);

        try(
                InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");
                ServletOutputStream out = response.getOutputStream();
            ){

            //基于提供好的模板文件创建一个新的Excel表格对象
            XSSFWorkbook excel = new XSSFWorkbook(in);
            //获得Excel文件中的一个Sheet页
            XSSFSheet sheet = excel.getSheet("Sheet1");

            sheet.getRow(1).getCell(1).setCellValue(begin + "至" + end);
            //获得第4行
            XSSFRow row = sheet.getRow(3);
            //获取单元格
            row.getCell(2).setCellValue(vo.getTurnover());
            row.getCell(4).setCellValue(vo.getOrderCompletionRate());
            row.getCell(6).setCellValue(vo.getNewUsers());
            row = sheet.getRow(4);
            row.getCell(2).setCellValue(vo.getValidOrderCount());
            row.getCell(4).setCellValue(vo.getUnitPrice());
            for (int i = 0; i < 30; i++) {
                LocalDate date = begin.plusDays(i).toLocalDate();
                //准备明细数据
                vo = workspaceService.getBusinessData(LocalDateTime.of(date,LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));
                row = sheet.getRow(7 + i);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(vo.getTurnover());
                row.getCell(3).setCellValue(vo.getValidOrderCount());
                row.getCell(4).setCellValue(vo.getOrderCompletionRate());
                row.getCell(5).setCellValue(vo.getUnitPrice());
                row.getCell(6).setCellValue(vo.getNewUsers());
            }
            //通过输出流将文件下载到客户端浏览器中

            excel.write(out);
        }catch (Exception e){
            e.printStackTrace();
            log.error("模板文件读取异常...");
        }
    }
}
