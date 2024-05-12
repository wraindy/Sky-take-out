create table order_detail
(
    id          bigint auto_increment comment '主键'
        primary key,
    name        varchar(32)    null comment '名字',
    image       varchar(512)   null comment '图片',
    order_id    bigint         not null comment '订单id',
    dish_id     bigint         null comment '菜品id',
    setmeal_id  bigint         null comment '套餐id',
    dish_flavor varchar(50)    null comment '口味',
    number      int default 1  not null comment '数量',
    amount      decimal(10, 2) not null comment '金额'
)
    comment '订单明细表' collate = utf8mb3_bin;

INSERT INTO sky_take_out.order_detail (id, name, image, order_id, dish_id, setmeal_id, dish_flavor, number, amount) VALUES (85, '佛跳墙', 'http://cangqiongwaimai.cn:49007/cqwm-dish/fd5a681d-378f-4b62-999b-ba7b85ffc804.jpg', 69, 93, null, '不辣,不要香菜,全糖,多冰', 1, 101.59);
INSERT INTO sky_take_out.order_detail (id, name, image, order_id, dish_id, setmeal_id, dish_flavor, number, amount) VALUES (86, '德国烤肠', 'http://cangqiongwaimai.cn:49007/cqwm-dish/18ac8acd-beb9-4083-b349-050f8c79177a.jpg', 70, 94, null, '重辣', 10, 5.90);
INSERT INTO sky_take_out.order_detail (id, name, image, order_id, dish_id, setmeal_id, dish_flavor, number, amount) VALUES (87, '佛跳墙', 'http://cangqiongwaimai.cn:49007/cqwm-dish/fd5a681d-378f-4b62-999b-ba7b85ffc804.jpg', 71, 93, null, '热饮,不辣,不要香菜,少糖', 1, 101.59);
