create table address_book
(
    id            bigint auto_increment comment '主键'
        primary key,
    user_id       bigint                       not null comment '用户id',
    consignee     varchar(50)                  null comment '收货人',
    sex           varchar(2)                   null comment '性别',
    phone         varchar(11)                  not null comment '手机号',
    province_code varchar(12) charset utf8mb4  null comment '省级区划编号',
    province_name varchar(32) charset utf8mb4  null comment '省级名称',
    city_code     varchar(12) charset utf8mb4  null comment '市级区划编号',
    city_name     varchar(32) charset utf8mb4  null comment '市级名称',
    district_code varchar(12) charset utf8mb4  null comment '区级区划编号',
    district_name varchar(32) charset utf8mb4  null comment '区级名称',
    detail        varchar(200) charset utf8mb4 null comment '详细地址',
    label         varchar(100) charset utf8mb4 null comment '标签',
    is_default    tinyint(1) default 0         not null comment '默认 0 否 1是'
)
    comment '地址簿' collate = utf8mb3_bin;

INSERT INTO sky_take_out.address_book (id, user_id, consignee, sex, phone, province_code, province_name, city_code, city_name, district_code, district_name, detail, label, is_default) VALUES (5, 4, '张三', '0', '18811111111', '44', '广东省', '4401', '广州市', '440103', '荔湾区', '智行工业园05座', '1', 0);
INSERT INTO sky_take_out.address_book (id, user_id, consignee, sex, phone, province_code, province_name, city_code, city_name, district_code, district_name, detail, label, is_default) VALUES (6, 4, '张三', '0', '18822222222', '21', '辽宁省', '2102', '大连市', '210202', '中山区', '金玉豪庭天守阁9楼B号', '2', 0);
INSERT INTO sky_take_out.address_book (id, user_id, consignee, sex, phone, province_code, province_name, city_code, city_name, district_code, district_name, detail, label, is_default) VALUES (7, 4, '李四', '1', '18833333333', '45', '广西壮族自治区', '4508', '贵港市', '450802', '港北区', '璃月港第一中学', '3', 1);
INSERT INTO sky_take_out.address_book (id, user_id, consignee, sex, phone, province_code, province_name, city_code, city_name, district_code, district_name, detail, label, is_default) VALUES (8, 4, '张三', '0', '18844444444', '11', '北京市', '1101', '市辖区', '110105', '朝阳区', '朝阳中学', '3', 0);
