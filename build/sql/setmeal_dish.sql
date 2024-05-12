create table setmeal_dish
(
    id         bigint auto_increment comment '主键'
        primary key,
    setmeal_id bigint         null comment '套餐id',
    dish_id    bigint         null comment '菜品id',
    name       varchar(32)    null comment '菜品名称 （冗余字段）',
    price      decimal(10, 2) null comment '菜品单价（冗余字段）',
    copies     int            null comment '菜品份数'
)
    comment '套餐菜品关系' collate = utf8mb3_bin;

INSERT INTO sky_take_out.setmeal_dish (id, setmeal_id, dish_id, name, price, copies) VALUES (75, 38, 90, '玉米排骨汤', 18.80, 8);
INSERT INTO sky_take_out.setmeal_dish (id, setmeal_id, dish_id, name, price, copies) VALUES (76, 38, 94, '德国烤肠', 5.90, 3);
INSERT INTO sky_take_out.setmeal_dish (id, setmeal_id, dish_id, name, price, copies) VALUES (77, 38, 91, '蛋炒饭', 20.00, 2);
INSERT INTO sky_take_out.setmeal_dish (id, setmeal_id, dish_id, name, price, copies) VALUES (78, 38, 93, '佛跳墙', 101.59, 1);
INSERT INTO sky_take_out.setmeal_dish (id, setmeal_id, dish_id, name, price, copies) VALUES (79, 39, 94, '德国烤肠', 5.90, 10);
INSERT INTO sky_take_out.setmeal_dish (id, setmeal_id, dish_id, name, price, copies) VALUES (80, 39, 91, '蛋炒饭', 20.00, 10);
INSERT INTO sky_take_out.setmeal_dish (id, setmeal_id, dish_id, name, price, copies) VALUES (81, 39, 93, '佛跳墙', 101.59, 10);
