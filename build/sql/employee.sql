create table employee
(
    id          bigint auto_increment comment '主键'
        primary key,
    name        varchar(32)   not null comment '姓名',
    username    varchar(32)   not null comment '用户名',
    password    varchar(64)   not null comment '密码',
    phone       varchar(11)   not null comment '手机号',
    sex         varchar(2)    not null comment '性别',
    id_number   varchar(18)   not null comment '身份证号',
    status      int default 1 not null comment '状态 0:禁用，1:启用',
    create_time datetime      null comment '创建时间',
    update_time datetime      null comment '更新时间',
    create_user bigint        null comment '创建人',
    update_user bigint        null comment '修改人',
    constraint idx_username
        unique (username)
)
    comment '员工信息' collate = utf8mb3_bin;

INSERT INTO sky_take_out.employee (id, name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user) VALUES (1, '管理员', 'admin', 'e10adc3949ba59abbe56e057f20f883e', '18812345678', '1', '000000000000000000', 1, '2022-02-15 15:51:20', '2024-04-21 20:18:50', 1, 1);
INSERT INTO sky_take_out.employee (id, name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user) VALUES (3, '张三', 'zhangsan', 'e10adc3949ba59abbe56e057f20f883e', '18800000000', '1', '000000000000000000', 1, '2024-04-18 20:13:21', '2024-05-11 19:32:40', 1, 1);
INSERT INTO sky_take_out.employee (id, name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user) VALUES (6, '李四', 'lisi', 'e10adc3949ba59abbe56e057f20f883e', '18811111111', '1', '000000000000000000', 0, '2024-04-18 20:17:37', '2024-05-11 19:34:19', 1, 1);
INSERT INTO sky_take_out.employee (id, name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user) VALUES (9, '王五', 'wangwu', 'e10adc3949ba59abbe56e057f20f883e', '18822222222', '1', '000000000000000000', 1, '2024-04-18 20:39:26', '2024-05-11 19:33:34', 1, 1);
INSERT INTO sky_take_out.employee (id, name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user) VALUES (16, '王小美', 'wangxiaomei', 'e10adc3949ba59abbe56e057f20f883e', '18833333333', '0', '000000000000000000', 1, '2024-04-21 18:22:27', '2024-05-11 19:34:07', 1, 1);
