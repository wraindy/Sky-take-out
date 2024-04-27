package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * @Author Wraindy
 * @DateTime 2024/04/27 22:00
 * Description
 * Notice
 **/
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    // 微信服务接口地址
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;


    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {

        // 调用微信接口，获取当前用户openID
        String result = getJsonResult(userLoginDTO.getCode());
        JSONObject resultObject = JSON.parseObject(result);

        // 如果获取失败，抛出业务异常
        String openId = resultObject.getString("openid");
        if(openId == null || openId.isEmpty()){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        // 判断是否是新用户（即openID是否未写入过user表中）
        User user = userMapper.getByOpenId(openId);
        // 如果是新用户，则需要将openID加入到用户表中
        if(user == null){
            // 这里不用@AutoFill注解是因为还没有足够的信息，比如创建人之类的
            user = User.builder()
                    .openid(openId)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }
        return user;
    }

    /**
     * 调用微信接口服务，获取包含openId的json字符串结果
     * @param jsCode
     * @return
     */
    private String getJsonResult(String jsCode){
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("appid", weChatProperties.getAppid());
        paramMap.put("secret", weChatProperties.getSecret());
        paramMap.put("js_code", jsCode);
        paramMap.put("grant_type", "authorization_code");
        return HttpClientUtil.doGet(WX_LOGIN, paramMap);
    }
}
