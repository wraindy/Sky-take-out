create table category
(
    id          bigint auto_increment comment '主键'
        primary key,
    type        int           null comment '类型   1 菜品分类 2 套餐分类',
    name        varchar(32)   not null comment '分类名称',
    sort        int default 0 not null comment '顺序',
    status      int           null comment '分类状态 0:禁用，1:启用',
    create_time datetime      null comment '创建时间',
    update_time datetime      null comment '更新时间',
    create_user bigint        null comment '创建人',
    update_user bigint        null comment '修改人',
    constraint idx_category_name
        unique (name)
)
    comment '菜品及套餐分类' collate = utf8mb3_bin;

INSERT INTO sky_take_out.category (id, type, name, sort, status, create_time, update_time, create_user, update_user) VALUES (30, 1, '汤类', 3, 1, '2024-05-11 23:49:36', '2024-05-12 00:15:41', 1, 1);
INSERT INTO sky_take_out.category (id, type, name, sort, status, create_time, update_time, create_user, update_user) VALUES (31, 2, '商务套餐', 5, 1, '2024-05-11 23:51:03', '2024-05-12 00:15:47', 1, 1);
INSERT INTO sky_take_out.category (id, type, name, sort, status, create_time, update_time, create_user, update_user) VALUES (32, 2, '团体套餐', 4, 1, '2024-05-11 23:51:33', '2024-05-12 00:15:44', 1, 1);
INSERT INTO sky_take_out.category (id, type, name, sort, status, create_time, update_time, create_user, update_user) VALUES (33, 1, '店长推荐', 0, 1, '2024-05-11 23:54:00', '2024-05-11 23:54:55', 1, 1);
INSERT INTO sky_take_out.category (id, type, name, sort, status, create_time, update_time, create_user, update_user) VALUES (34, 1, '主食', 1, 1, '2024-05-11 23:56:11', '2024-05-11 23:57:18', 1, 1);
INSERT INTO sky_take_out.category (id, type, name, sort, status, create_time, update_time, create_user, update_user) VALUES (35, 1, '小吃', 2, 1, '2024-05-11 23:56:22', '2024-05-12 00:15:37', 1, 1);
