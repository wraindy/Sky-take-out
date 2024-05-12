create table orders
(
    id                      bigint auto_increment comment '主键'
        primary key,
    number                  varchar(50)          null comment '订单号',
    status                  int        default 1 not null comment '订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消 7退款',
    user_id                 bigint               not null comment '下单用户',
    address_book_id         bigint               not null comment '地址id',
    order_time              datetime             not null comment '下单时间',
    checkout_time           datetime             null comment '结账时间',
    pay_method              int        default 1 not null comment '支付方式 1微信,2支付宝',
    pay_status              tinyint    default 0 not null comment '支付状态 0未支付 1已支付 2退款',
    amount                  decimal(10, 2)       not null comment '实收金额',
    remark                  varchar(100)         null comment '备注',
    phone                   varchar(11)          null comment '手机号',
    address                 varchar(255)         null comment '地址',
    user_name               varchar(32)          null comment '用户名称',
    consignee               varchar(32)          null comment '收货人',
    cancel_reason           varchar(255)         null comment '订单取消原因',
    rejection_reason        varchar(255)         null comment '订单拒绝原因',
    cancel_time             datetime             null comment '订单取消时间',
    estimated_delivery_time datetime             null comment '预计送达时间',
    delivery_status         tinyint(1) default 1 not null comment '配送状态  1立即送出  0选择具体时间',
    delivery_time           datetime             null comment '送达时间',
    pack_amount             int                  null comment '打包费',
    tableware_number        int                  null comment '餐具数量',
    tableware_status        tinyint(1) default 1 not null comment '餐具数量状态  1按餐量提供  0选择具体数量'
)
    comment '订单表' collate = utf8mb3_bin;

INSERT INTO sky_take_out.orders (id, number, status, user_id, address_book_id, order_time, checkout_time, pay_method, pay_status, amount, remark, phone, address, user_name, consignee, cancel_reason, rejection_reason, cancel_time, estimated_delivery_time, delivery_status, delivery_time, pack_amount, tableware_number, tableware_status) VALUES (69, '1715444640869', 5, 4, 7, '2024-05-12 00:24:01', '2024-05-12 00:24:03', 1, 1, 108.59, '快点送过来好饿！', '18833333333', '广西壮族自治区贵港市璃月港第一中学', null, '李四', null, null, null, '2024-05-12 01:23:00', 0, '2024-05-12 00:24:30', 1, 10, 0);
INSERT INTO sky_take_out.orders (id, number, status, user_id, address_book_id, order_time, checkout_time, pay_method, pay_status, amount, remark, phone, address, user_name, consignee, cancel_reason, rejection_reason, cancel_time, estimated_delivery_time, delivery_status, delivery_time, pack_amount, tableware_number, tableware_status) VALUES (70, '1715444749715', 6, 4, 8, '2024-05-12 00:25:50', '2024-05-12 00:25:51', 1, 2, 75.00, '放多点辣椒', '18844444444', '北京市市辖区朝阳中学', null, '张三', '<用户主动取消订单>', null, '2024-05-12 00:26:04', '2024-05-12 01:25:00', 0, null, 10, 0, 0);
INSERT INTO sky_take_out.orders (id, number, status, user_id, address_book_id, order_time, checkout_time, pay_method, pay_status, amount, remark, phone, address, user_name, consignee, cancel_reason, rejection_reason, cancel_time, estimated_delivery_time, delivery_status, delivery_time, pack_amount, tableware_number, tableware_status) VALUES (71, '1715497865935', 5, 4, 7, '2024-05-12 15:11:06', '2024-05-12 15:11:07', 1, 1, 108.59, '', '18833333333', '广西壮族自治区贵港市璃月港第一中学', null, '李四', null, null, null, '2024-05-12 16:11:00', 0, '2024-05-12 15:11:41', 1, 0, 0);
