package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Author Wraindy
 * @DateTime 2024/04/27 22:59
 * Description
 * Notice
 **/
@Mapper
public interface UserMapper {

    /**
     * 根据openId获取用户信息
     * @param openId
     * @return
     */
    @Select("select * from user where openid = #{openId}")
    User getByOpenId(String openId);

    /**
     * 根据插入用户信息（需要返回主键给实体类）
     * @param user
     */
    void insert(User user);

    /**
     * 根据用户id获取用户实体类
     * @param userId
     * @return
     */
    @Select("select * from user where id = #{userId}")
    User getById(Long userId);
}
