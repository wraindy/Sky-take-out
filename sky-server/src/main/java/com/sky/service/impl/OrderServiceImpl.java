package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.*;
import com.sky.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author Wraindy
 * @DateTime 2024/05/02 16:38
 * Description
 * Notice
 **/
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WeChatPayUtil weChatPayUtil;

    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * 用户下单
     * @param ordersSubmitDTO
     */
    @Override
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {

        // 校验地址簿是否为空
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null){
            // 地址不存在，抛出业务异常
            throw new OrderBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        // 校验购物车是否为空
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(ShoppingCart
                .builder()
                .userId(BaseContext.getCurrentId())
                .build());
        if (shoppingCartList == null || shoppingCartList.isEmpty()){
            // 购物车数据不存在，抛出业务异常
            throw new OrderBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        // 构造订单主表数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);

        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setUserId(BaseContext.getCurrentId());
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        // 实收总金额
        BigDecimal totalMoney = new BigDecimal(0);
        // 总打包次数
        int totalPackNumber = 0;

        // 地址信息快照，微信登录的小程序没有用户名字段，因此忽略user_name字段
        orders.setPhone(addressBook.getPhone());
        orders.setAddress(addressBook.getProvinceName()+
                addressBook.getCityName()+
                addressBook.getDetail());
        orders.setConsignee(addressBook.getConsignee());
        orders.setTablewareNumber(ordersSubmitDTO.getTablewareNumber());

        // 构造订单明细表数据
        List<OrderDetail> odList = new ArrayList<>();
        for(ShoppingCart sc : shoppingCartList){
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(sc, orderDetail);
            // 上面orderMapper.insert(orders)会返回主键值
            orderDetail.setOrderId(Long.parseLong(orders.getNumber()));
            odList.add(orderDetail);

            // 计算菜品费用（单价*数量）
            totalMoney = totalMoney.add(orderDetail.getAmount().multiply(new BigDecimal(orderDetail.getNumber())));
            // 计算打包次数（份/次）
            totalPackNumber += sc.getNumber();
        }

        // 计算总打包费（次/1元）
        BigDecimal totalPackAmount = new BigDecimal(1).multiply(new BigDecimal(totalPackNumber));

        // 计算总实收金额（菜品费用 + 配送费 6元/次 + 打包费 次/1元）
        orders.setAmount(totalMoney.add(totalPackAmount).add(new BigDecimal(6)));

        // 插入订单表和订单明细表
        orderMapper.insert(orders);
        orderDetailMapper.insertBatch(odList);

        // 清空购物车数据
        shoppingCartMapper.cleanAllByUserId(BaseContext.getCurrentId());

        // 返回VO
        return OrderSubmitVO
                .builder()
                .id(orders.getId())
                .orderTime(orders.getOrderTime())
                .orderAmount(orders.getAmount())
                .orderNumber(orders.getNumber())
                .build();
    }

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    @Override
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        //调用微信支付接口，生成预支付交易单
        JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), //商户订单号
                new BigDecimal(0.01), //支付金额，单位 元
                "苍穹外卖订单", //商品描述
                user.getOpenid() //微信用户的openid
        );

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;
    }

    /**
     * 微信支付成功，修改订单状态
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        // todo 订单号应融合用户信息，校验也是如此，这里默认订单号唯一了
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);

        // 推送订单消息给管理端
        // type 1-来单提醒 2-客户催单
        Map<String, Object> map = new HashMap();
        map.put("type", 1);
        map.put("orderId", ordersDB.getId());
        map.put("content", "订单号：" + outTradeNo);
        //通过WebSocket实现来单提醒，向客户端浏览器推送消息
        webSocketServer.sendToAllClient(JSON.toJSONString(map));
    }

    /**
     * 订单支付（除去微信支付的版本）
     * @param ordersPaymentDTO
     * @return
     * payment2与payment方法相比，直接修改订单数据，
     * 然后删去了后端调用微信支付接口生成预支付交易单的功能，
     * 接着直接向小程序返回调起支付所需的假的参数（https://pay.weixin.qq.com/docs/merchant/apis/mini-program-payment/mini-transfer-payment.html）
     */
    @Override
    public OrderPaymentVO payment2(OrdersPaymentDTO ordersPaymentDTO) {
        // 修改订单状态
        this.paySuccess(ordersPaymentDTO.getOrderNumber());
        log.info("\n已执行模拟微信支付-----");
        // 返回假参数
        return OrderPaymentVO
                .builder()
                .timeStamp("123456789")
                .nonceStr("随机字符串")
                .packageStr("prepay_id=123456789")
                .paySign("签名：（appid+timeStamp+nonceStr+package）")
                .signType("RSA")
                .build();
    }

    /**
     * 用户查询自己的历史订单记录
     * @param ordersPageQueryDTO
     * @return
     */
    @Override
    public PageResult queryHistory(OrdersPageQueryDTO ordersPageQueryDTO) {

        // 设置用户id
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());

        // 分页查询订单主表
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        List<Orders> list = orderMapper.queryByCondition(ordersPageQueryDTO);

        // 判断主表查询结果是否为空
        if (list == null || list.isEmpty()){
            return new PageResult(0, null);
        }

        // 构造List<OrderVO>
        List<OrderVO> orderVOList = list.stream().map(orders -> {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(orders, orderVO);
            orderVO.setOrderDetailList(orderDetailMapper.getByNumber(orders.getNumber()));
            return orderVO;
        }).collect(Collectors.toList());

        return new PageResult(orderVOList.size(), orderVOList);
    }

    /**
     * 根据订单表主键查询订单详情
     * @param id
     * @return
     */
    @Override
    public OrderVO queryOrderDetail(Long id) {
        Orders orders = orderMapper.getById(id);
        List<OrderDetail> orderDetailList = orderDetailMapper.getByNumber(orders.getNumber());
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetailList);
        return orderVO;
    }

    /**
     * 动态查询订单
     * @param ordersPageQueryDTO
     * @return
     */
    @Override
    public PageResult queryByCondition(OrdersPageQueryDTO ordersPageQueryDTO) {

        // 分页查询订单主表
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        List<Orders> list = orderMapper.queryByCondition(ordersPageQueryDTO);

        // 判断主表查询结果是否为空
        if (list == null || list.isEmpty()){
            return new PageResult(0, null);
        }

        // 填充dishString并转换成OrderVO集合
        List<OrderVO> orderVOS = transferOrderVOList(list);

        return new PageResult(orderVOS.size(), orderVOS);
    }

    /**
     * 各个状态的订单数量统计
     *
     * @return
     */
    @Override
    public OrderStatisticsVO getStatistics() {

        List<Integer> statusList = orderMapper.getStatistics();
        if (statusList == null || statusList.isEmpty()){
            // 没有订单就直接返回空数据
            return new OrderStatisticsVO();
        }

        Map<Integer, Integer> statusCount = new HashMap<>();
        for (Integer status : statusList) {
            statusCount.put(status, statusCount.getOrDefault(status, 0) + 1);
        }

        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        // 待接单、待派送、派送中
        orderStatisticsVO.setToBeConfirmed(statusCount.getOrDefault(Orders.TO_BE_CONFIRMED, 0));
        orderStatisticsVO.setConfirmed(statusCount.getOrDefault(Orders.CONFIRMED, 0));
        orderStatisticsVO.setDeliveryInProgress(statusCount.getOrDefault(Orders.DELIVERY_IN_PROGRESS, 0));

        return orderStatisticsVO;
    }

    /**
     * 商家接单
     * @param id
     */
    @Override
    public void confirm(Long id) {
        orderMapper.confirm(id);
    }

    /**
     * 商家拒单
     * 只有<待接单2>的状态才能拒单
     * @param ordersRejectionDTO
     */
    @Override
    public void rejection(OrdersRejectionDTO ordersRejectionDTO) {

        // 查询订单
        Orders orders = orderMapper.getById(ordersRejectionDTO.getId());

        // 订单不存在
        if (orders == null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        // 如果不是<待接单2>状态，则无法拒单，响应订单状态错误
        if (!Objects.equals(orders.getStatus(), Orders.TO_BE_CONFIRMED)){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        // 如果付款了还要退款
        if (Objects.equals(orders.getPayStatus(), Orders.PAID)) {
//            weChatPayUtil.refund(
//                    orders.getNumber(),
//                    orders.getNumber(),
//                    new BigDecimal(0.1),
//                    new BigDecimal(0.1));
//        }
            log.info("模拟微信支付退款<拒单操作>：{}", orders);
            orders.setPayStatus(Orders.REFUND);
        }

        orders.setStatus(Orders.CANCELLED);
        orders.setRejectionReason(ordersRejectionDTO.getRejectionReason());
        orders.setCancelTime(LocalDateTime.now());

        orderMapper.update(orders);
    }

    /**
     * 商家取消订单
     * 只有<待付款1><待派送3><派送中4><已完成5>才能取消订单
     * 注意：<待派送3> === 数据库中orders表status字段的<已接单3>
     * @param ordersCancelDTO
     */
    @Override
    public void cancel(OrdersCancelDTO ordersCancelDTO) {
        // 查询订单
        Orders orders = orderMapper.getById(ordersCancelDTO.getId());

        // 订单不存在
        if (orders == null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        if (orders.getStatus() == null ||
                orders.getStatus().equals(Orders.TO_BE_CONFIRMED) ||
                orders.getStatus().equals(Orders.CANCELLED)){
            // todo Orders表中不知为何status字段有个<退款7>，暂时无视
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        // 如果付款了还要退款
        if (Objects.equals(orders.getPayStatus(), Orders.PAID)) {
//            weChatPayUtil.refund(
//                    orders.getNumber(),
//                    orders.getNumber(),
//                    new BigDecimal(0.1),
//                    new BigDecimal(0.1));
//        }
            log.info("模拟微信支付退款<商家取消订单>：{}", orders);
            orders.setPayStatus(Orders.REFUND);
        }

        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason(ordersCancelDTO.getCancelReason());
        orders.setCancelTime(LocalDateTime.now());

        orderMapper.update(orders);
    }

    /**
     * 用户取消订单
     * 只有<待付款1><待接单2>状态才能取消订单
     * @param id
     */
    @Override
    public void userCancel(Long id) {
        // 查询订单
        Orders orders = orderMapper.getById(id);

        // 订单不存在
        if (orders == null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        if (!Objects.equals(orders.getStatus(), Orders.PENDING_PAYMENT) &&
                !orders.getStatus().equals(Orders.TO_BE_CONFIRMED)){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        // 如果付款了还要退款
        if (Objects.equals(orders.getPayStatus(), Orders.PAID)) {
//            weChatPayUtil.refund(
//                    orders.getNumber(),
//                    orders.getNumber(),
//                    new BigDecimal(0.1),
//                    new BigDecimal(0.1));
//        }
            log.info("模拟微信支付退款<商家取消订单>：{}", orders);
            orders.setPayStatus(Orders.REFUND);
        }

        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason("<用户主动取消订单>");
        orders.setCancelTime(LocalDateTime.now());

        orderMapper.update(orders);
    }

    /**
     * 用户再来一单
     * @param id
     */
    @Override
    public void repetition(Long id) {
        Long orderId = orderMapper.getNumberById(id);
        List<OrderDetail> odList = orderDetailMapper.getByNumber(String.valueOf(orderId));
        LocalDateTime now = LocalDateTime.now();
        List<ShoppingCart> scList = odList.stream().map(od -> {
            ShoppingCart sc = new ShoppingCart();
            BeanUtils.copyProperties(od, sc);
            sc.setCreateTime(now);
            sc.setUserId(BaseContext.getCurrentId());
            return sc;
        }).collect(Collectors.toList());
        shoppingCartMapper.insertBatch(scList);
    }

    /**
     * 商家派单
     * @param id
     */
    @Override
    public void delivery(Long id) {
        Orders orders = orderMapper.getById(id);
        // 订单不存在
        if (orders == null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        if (!Objects.equals(orders.getStatus(), Orders.CONFIRMED)){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        orders.setStatus(Orders.DELIVERY_IN_PROGRESS);
        orderMapper.update(orders);
    }

    @Override
    public void complete(Long id) {
        Orders orders = orderMapper.getById(id);
        // 订单不存在
        if (orders == null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        if (!Objects.equals(orders.getStatus(), Orders.DELIVERY_IN_PROGRESS)){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        orders.setStatus(Orders.COMPLETED);
        orderMapper.update(orders);
    }

    /**
     * 用户催单
     * @param id
     */
    @Override
    public void reminder(Long id) {

        Orders byId = orderMapper.getById(id);
        if (byId == null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        Map<String, Object> map = new HashMap<>();

        // 客户催单
        map.put("type", 2);
        map.put("orderId", byId.getNumber());
        map.put("content", "订单号" + byId.getNumber());


        webSocketServer.sendToAllClient(JSON.toJSONString(map));
    }


    /**
     * 将List<Orders>转换成List<OrderVO>
     * @param ordersList
     * @return
     */
    private List<OrderVO> transferOrderVOList(List<Orders> ordersList){
        List<OrderVO> orderVOList = new ArrayList<>();
        for (Orders orders : ordersList){
            List<OrderDetail> orderDetailList = orderDetailMapper.getDishStringItem(orders.getNumber());
            String dishString = getDishString(orderDetailList);
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(orders, orderVO);
            orderVO.setOrderDishes(dishString);
            orderVOList.add(orderVO);
        }
        return orderVOList;
    }

    /**
     * 单个订单对应的订单详情集合，生成菜品字符串
     * @param orderDetailList
     * @return
     */
    private String getDishString(List<OrderDetail> orderDetailList){
        List<String> collect = orderDetailList.stream().map(od -> od.getName() + '*' + od.getNumber()).collect(Collectors.toList());
        return String.join(" ", collect);
    }

}
