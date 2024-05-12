create table user
(
    id          bigint auto_increment comment '主键'
        primary key,
    openid      varchar(45)  null comment '微信用户唯一标识',
    name        varchar(32)  null comment '姓名',
    phone       varchar(11)  null comment '手机号',
    sex         varchar(2)   null comment '性别',
    id_number   varchar(18)  null comment '身份证号',
    avatar      varchar(500) null comment '头像',
    create_time datetime     null
)
    comment '用户信息' collate = utf8mb3_bin;

INSERT INTO sky_take_out.user (id, openid, name, phone, sex, id_number, avatar, create_time) VALUES (4, '123', null, null, null, null, null, '2024-04-27 23:22:28');
