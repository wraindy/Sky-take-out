create table dish
(
    id          bigint auto_increment comment '主键'
        primary key,
    name        varchar(32)    not null comment '菜品名称',
    category_id bigint         not null comment '菜品分类id',
    price       decimal(10, 2) null comment '菜品价格',
    image       varchar(512)   null comment '图片',
    description varchar(255)   null comment '描述信息',
    status      int default 1  null comment '0 停售 1 起售',
    create_time datetime       null comment '创建时间',
    update_time datetime       null comment '更新时间',
    create_user bigint         null comment '创建人',
    update_user bigint         null comment '修改人',
    constraint idx_dish_name
        unique (name)
)
    comment '菜品' collate = utf8mb3_bin;

INSERT INTO sky_take_out.dish (id, name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) VALUES (90, '玉米排骨汤', 30, 18.80, 'http://cangqiongwaimai.cn:49007/cqwm-dish/605bf56d-485a-4c4a-9f7c-4498a293c6e4.jpg', '精心制作', 1, '2024-05-11 23:50:27', '2024-05-11 23:50:31', 1, 1);
INSERT INTO sky_take_out.dish (id, name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) VALUES (91, '蛋炒饭', 34, 20.00, 'http://cangqiongwaimai.cn:49007/cqwm-dish/8ebeb700-63cf-4ac1-89c4-b2d848c3ad38.jpg', '分量很大！', 1, '2024-05-12 00:01:45', '2024-05-12 00:03:06', 1, 1);
INSERT INTO sky_take_out.dish (id, name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) VALUES (92, '咖喱猪扒饭', 34, 15.00, 'http://cangqiongwaimai.cn:49007/cqwm-dish/78648abe-f6cc-4125-bc12-82a2c9aea3ff.jpg', '美味咖喱哈哈哈', 1, '2024-05-12 00:03:01', '2024-05-12 00:03:04', 1, 1);
INSERT INTO sky_take_out.dish (id, name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) VALUES (93, '佛跳墙', 33, 101.59, 'http://cangqiongwaimai.cn:49007/cqwm-dish/fd5a681d-378f-4b62-999b-ba7b85ffc804.jpg', '顶级食材', 1, '2024-05-12 00:08:35', '2024-05-12 00:20:11', 1, 1);
INSERT INTO sky_take_out.dish (id, name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) VALUES (94, '德国烤肠', 35, 5.99, 'http://cangqiongwaimai.cn:49007/cqwm-dish/18ac8acd-beb9-4083-b349-050f8c79177a.jpg', '美味烤肠', 1, '2024-05-12 00:15:17', '2024-05-12 15:05:33', 1, 1);
