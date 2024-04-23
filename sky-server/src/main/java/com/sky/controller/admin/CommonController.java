package com.sky.controller.admin;

import com.sky.config.MinioConfig;
import com.sky.result.Result;
import com.sky.util.MinioUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @Author Wraindy
 * @DateTime 2024/04/23 19:02
 * Description
 * Notice
 **/
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {

    @Autowired
    private MinioUtil minioUtil;

    @Autowired
    private MinioConfig minioConfig;

    @PostMapping("upload")
    @ApiOperation("文件上传")
    public Result<String> upload(@RequestParam("file") MultipartFile multipartFile){

        // todo 文件名合法性校验（这里默认文件名正常"xxx.xxx"，格式是图片类型）、文件大小合法性校验
        String extName = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
        String objectName = UUID.randomUUID() + extName;

        // 上传文件
        minioUtil.uploadMultipleFile(minioConfig.getBucketNameDish(),objectName, multipartFile);

        // 返回预览地址
        String url = minioUtil.getObjectUrl(minioConfig.getBucketNameDish(),objectName, true);

        return Result.success(url);
    }
}
