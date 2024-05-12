create table setmeal
(
    id          bigint auto_increment comment '主键'
        primary key,
    category_id bigint         not null comment '菜品分类id',
    name        varchar(32)    not null comment '套餐名称',
    price       decimal(10, 2) not null comment '套餐价格',
    status      int default 1  null comment '售卖状态 0:停售 1:起售',
    description varchar(255)   null comment '描述信息',
    image       varchar(512)   null comment '图片',
    create_time datetime       null comment '创建时间',
    update_time datetime       null comment '更新时间',
    create_user bigint         null comment '创建人',
    update_user bigint         null comment '修改人',
    constraint idx_setmeal_name
        unique (name)
)
    comment '套餐' collate = utf8mb3_bin;

INSERT INTO sky_take_out.setmeal (id, category_id, name, price, status, description, image, create_time, update_time, create_user, update_user) VALUES (38, 31, '白领办公套餐', 999.00, 1, '白领人士都喜欢吃', 'http://cangqiongwaimai.cn:49007/cqwm-dish/1ddfe512-56a3-4f48-afb8-9c149214b908.jpg', '2024-05-12 00:19:58', '2024-05-12 00:20:16', 1, 1);
INSERT INTO sky_take_out.setmeal (id, category_id, name, price, status, description, image, create_time, update_time, create_user, update_user) VALUES (39, 32, '班级活动套餐', 666.66, 1, '班级举办活动专用！', 'http://cangqiongwaimai.cn:49007/cqwm-dish/cacc2908-41ee-4e95-a6a6-bf101810810a.jpg', '2024-05-12 00:22:55', '2024-05-12 00:22:57', 1, 1);
