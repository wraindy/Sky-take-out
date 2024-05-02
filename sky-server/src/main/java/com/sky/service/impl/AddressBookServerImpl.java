package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author Wraindy
 * @DateTime 2024/05/02 11:42
 * Description
 * Notice
 **/
@Service
public class AddressBookServerImpl implements AddressBookServer {

    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * 查询当前登录用户的所有地址信息
     * @return
     */
    @Override
    public List<AddressBook> list() {
        Long userId = BaseContext.getCurrentId();
        return addressBookMapper.getByUserId(userId);
    }

    /**
     * 根据id查询地址
     * @param addressBookId
     * @return
     */
    @Override
    public AddressBook getById(Long addressBookId) {
        return addressBookMapper.getById(addressBookId);
    }

    /**
     * 查询默认地址
     * @return
     */
    @Override
    public AddressBook listDefault() {
        Long userId = BaseContext.getCurrentId();
        return addressBookMapper.listDefault(userId);
    }

    /**
     * 设置默认地址
     * @param addressBookId
     */
    @Override
    @Transactional
    public void setDefault(Long addressBookId) {
        Long userId = BaseContext.getCurrentId();
        addressBookMapper.setNotDefault(userId);
        AddressBook addressBook = AddressBook
                .builder()
                .id(addressBookId)
                .isDefault(StatusConstant.ENABLE)
                .build();
        addressBookMapper.update(addressBook);
    }

    /**
     * 新增地址
     * @param addressBook
     */
    @Override
    public void add(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookMapper.insert(addressBook);
    }

    /**
     * 修改地址
     * @param addressBook
     */
    @Override
    public void update(AddressBook addressBook) {
        // todo 参数校验
        addressBookMapper.update(addressBook);
    }

    /**
     * 删除地址
     * @param addressBookId
     */
    @Override
    public void delete(Long addressBookId) {
        addressBookMapper.deleteById(addressBookId);
    }
}
