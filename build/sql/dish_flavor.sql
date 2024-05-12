create table dish_flavor
(
    id      bigint auto_increment comment '主键'
        primary key,
    dish_id bigint       not null comment '菜品',
    name    varchar(32)  null comment '口味名称',
    value   varchar(255) null comment '口味数据list'
)
    comment '菜品口味关系表' collate = utf8mb3_bin;

INSERT INTO sky_take_out.dish_flavor (id, dish_id, name, value) VALUES (167, 90, '温度', '["热饮","常温","去冰","少冰","多冰"]');
INSERT INTO sky_take_out.dish_flavor (id, dish_id, name, value) VALUES (168, 91, '辣度', '["不辣","微辣","中辣","重辣"]');
INSERT INTO sky_take_out.dish_flavor (id, dish_id, name, value) VALUES (169, 91, '忌口', '["不要葱","不要蒜","不要香菜","不要辣"]');
INSERT INTO sky_take_out.dish_flavor (id, dish_id, name, value) VALUES (170, 92, '辣度', '["不辣","微辣","中辣","重辣"]');
INSERT INTO sky_take_out.dish_flavor (id, dish_id, name, value) VALUES (171, 93, '温度', '["热饮","常温","去冰","少冰","多冰"]');
INSERT INTO sky_take_out.dish_flavor (id, dish_id, name, value) VALUES (172, 93, '甜味', '["无糖","少糖","半糖","多糖","全糖"]');
INSERT INTO sky_take_out.dish_flavor (id, dish_id, name, value) VALUES (173, 93, '忌口', '["不要葱","不要蒜","不要香菜","不要辣"]');
INSERT INTO sky_take_out.dish_flavor (id, dish_id, name, value) VALUES (174, 93, '辣度', '["不辣","微辣","中辣","重辣"]');
INSERT INTO sky_take_out.dish_flavor (id, dish_id, name, value) VALUES (176, 94, '辣度', '["不辣","微辣","中辣","重辣"]');
