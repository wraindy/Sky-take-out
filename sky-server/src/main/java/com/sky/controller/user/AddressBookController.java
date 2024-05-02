package com.sky.controller.user;

import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author Wraindy
 * @DateTime 2024/05/02 11:33
 * Description
 * Notice
 **/
@RestController
@Slf4j
@Api(tags = "用户地址相关接口")
@RequestMapping("/user/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookServer addressBookServer;

    @GetMapping("/list")
    @ApiOperation("查询当前登录用户的所有地址信息")
    public Result<List<AddressBook>> list(){
        List<AddressBook> list = addressBookServer.list();
        return Result.success(list);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询地址")
    public Result<AddressBook> getById(@PathVariable Long addressBookId){
        AddressBook ab = addressBookServer.getById(addressBookId);
        return Result.success(ab);
    }

    @GetMapping("/default")
    @ApiOperation("查询默认地址")
    public Result<AddressBook> listDefault(){
        AddressBook ab = addressBookServer.listDefault();
        return Result.success(ab);
    }
}
