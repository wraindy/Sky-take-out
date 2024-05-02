package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author Wraindy
 * @DateTime 2024/05/02 11:43
 * Description
 * Notice
 **/
@Mapper
public interface AddressBookMapper {
    /**
     * 查询当前登录用户的所有地址信息
     * @param userId
     * @return
     */
    @Select("select * from address_book where user_id = #{userId}")
    List<AddressBook> getByUserId(Long userId);

    /**
     * 根据id查询地址
     * @param addressBookId
     * @return
     */
    @Select("select * from address_book where id = #{addressBookId}")
    AddressBook getById(Long addressBookId);

    /**
     * 查询默认地址
     * @param userId
     * @return
     */
    @Select("select * from address_book where user_id = #{userId} and is_default = 1")
    AddressBook listDefault(Long userId);
}
