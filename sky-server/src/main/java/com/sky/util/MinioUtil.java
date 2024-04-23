package com.sky.util;

import com.sky.exception.MinioUtilException;
import io.minio.*;

import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;

/**
 * @Author Wraindy
 * @DateTime 2024/04/22 20:16
 * Description minio工具类
 * Notice
 **/
@Component
@Slf4j
public class MinioUtil {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.expire-time}")
    private int expireTime;

    /**
     * 判断桶是否存在
     * @param bucketName 桶名
     * @return 要么存在true要么不存在false，不然抛MinioUtilException
     */
    public Boolean bucketExists(String bucketName) {
        boolean found;
        try {
            found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            throw new MinioUtilException("网络异常，无法判断bucket <" + bucketName + "> 是否存在："+ e);
        }
        return found;
    }

    /**
     * 判断对象是否存在
     * @param bucketName 桶名
     * @param objectName 对象名
     * @return 存在就true，其他情况（包括网络问题）就false
     */
    public Boolean objectExists(String bucketName, String objectName) {
        if(!bucketExists(bucketName)){
            throw new MinioUtilException("bucket <" + bucketName + "> 不存在");
        }
        StatObjectArgs args = StatObjectArgs
                .builder()
                .bucket(bucketName)
                .object(objectName)
                .build();
        try {
            minioClient.statObject(args);
        } catch (Exception e){
            return false;
        }
        return true;
    }

    /**
     * 获取对象下载url
     * @param bucketName 桶名
     * @param objectName 对象名
     * @return 对象存在必定返回url，其他情况抛异常
     */
    public String getObjectUrl(String bucketName, String objectName){

        if(!objectExists(bucketName, objectName)){
            throw new MinioUtilException("bucket <"+bucketName+"> 存在，但"
                    +"object <"+objectName+"> 不存在");
        }

        String url;
        GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs
                .builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(objectName)
                .expiry(expireTime, TimeUnit.MINUTES)
                .build();
        try {
            url = minioClient.getPresignedObjectUrl(args);
        } catch (Exception e){
            throw new MinioUtilException("获取url失败");
        }
        return url;
    }

    /**
     * 上传文件
     * @param bucketName 桶名
     * @param objectName 对象名
     * @param inputStream 输入流
     * @param objectSize 对象大小（字节）
     */
    public void upload(String bucketName, String objectName, InputStream inputStream, long objectSize){

        // 校验bucket
        if (!bucketExists(bucketName)){
            throw new MinioUtilException("bucket <" + bucketName + "> 不存在");
        }
        //校验objectName
        if (StringUtils.isBlank(objectName)){
            throw new MinioUtilException("objectName不合法：<" + objectName + ">");
        }

        // 选择合适的contentType
        String contentType = URLConnection.guessContentTypeFromName(objectName);
        if (StringUtils.isBlank(contentType)) {
            // 如果无法确定类型，使用默认类型
            contentType = "application/octet-stream";
        }

        try{
            PutObjectArgs args = PutObjectArgs
                    .builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(inputStream, objectSize, -1)
                    .contentType(contentType)
                    .build();
            minioClient.putObject(args);
        } catch (Exception e){
            throw new MinioUtilException("网络异常，文件上传失败");
        }
    }

    /**
     * 上传uploadMultipleFile
     * @param bucketName 桶名
     * @param objectName 对象名
     * @param multipartFile MultipartFile
     */
    public void uploadMultipleFile(String bucketName, String objectName, MultipartFile multipartFile) {
        InputStream inputStream;
        try {
            inputStream = multipartFile.getInputStream();
        } catch (IOException e){
            throw new MinioUtilException("MultipartFile输入流获取失败");
        }
        upload(bucketName, objectName, inputStream, multipartFile.getSize());
    }
}
