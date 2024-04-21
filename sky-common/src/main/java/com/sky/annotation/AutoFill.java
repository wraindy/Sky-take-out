package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author Wraindy
 * @DateTime 2024/04/21 15:44
 * Description 标识某些方法需要自动填充公共字段
 * Notice 公共字段：createTime、updateTime、createUser、updateUser等（本项目就是这四个字段）
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    // 指定数据库的操作类型
    OperationType value();
}
