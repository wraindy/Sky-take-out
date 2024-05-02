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
     * @param id
     * @return
     */
    AddressBook getById(Long id);

    /**
     * 查询默认地址
     * @return
     */
    AddressBook listDefault();
}
