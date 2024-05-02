package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

/**
 * @Author Wraindy
 * @DateTime 2024/05/02 11:41
 * Description
 * Notice
 **/

public interface AddressBookServer {
    /**
     * 查询当前登录用户的所有地址信息
     * @return
     */
    List<AddressBook> list();

    /**
     * 根据id查询地址
     * @param addressBookId
     * @return
     */
    AddressBook getById(Long addressBookId);

    /**
     * 查询默认地址
     * @return
     */
    AddressBook listDefault();

    /**
     * 设置默认地址
     * @param addressBookId
     */
    void setDefault(Long addressBookId);

    /**
     * 新增地址
     * @param addressBook
     */
    void add(AddressBook addressBook);

    /**
     * 修改地址
     * @param addressBook
     */
    void update(AddressBook addressBook);
}
