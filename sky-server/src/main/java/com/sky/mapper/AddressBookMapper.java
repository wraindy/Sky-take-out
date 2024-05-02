package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.*;

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

    /**
     * 取消默认地址的设置
     * @param userId
     */
    @Update("update address_book set is_default = 0 where user_id = #{userId} and is_default = 1")
    void setNotDefault(Long userId);

    /**
     * 修改地址信息
     * @param addressBook
     */
    void update(AddressBook addressBook);

    /**
     * 新增地址
     * @param addressBook
     */
    @Insert("insert into address_book(user_id, consignee, sex, phone, province_code, province_name, city_code, city_name, district_code, district_name, detail, label, is_default)" +
            "VALUES (#{userId}, #{consignee}, #{sex}, #{phone}, #{provinceCode}, #{provinceName}, #{cityCode}, #{cityName}, #{districtCode}, #{districtName}, #{detail}, #{label}, 0)")
    void insert(AddressBook addressBook);

    /**
     * 删除地址
     * @param addressBookId
     */
    @Delete("delete from address_book where id = #{addressBookId}")
    void deleteById(Long addressBookId);
}
