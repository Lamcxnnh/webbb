package com.seig.rental.common.utils;

import com.seig.rental.common.config.MinioConfig;
import com.seig.rental.common.exception.BusinessException;
import io.minio.*;
import io.minio.http.Method;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * MinIO 文件上传工具类
 */
@Slf4j
@Component
public class MinioUtil {

    @Resource
    private MinioClient minioClient;

    @Resource
    private MinioConfig minioConfig;

    /**
     * 上传文件
     *
     * @param file      文件
     * @param dirPrefix 目录前缀 (如 "apartment", "room", "avatar")
     * @return 文件访问URL
     */
    public String upload(MultipartFile file, String dirPrefix) {
        try {
            String originalFilename = file.getOriginalFilename();
            String suffix = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String objectName = dirPrefix + "/" + UUID.randomUUID().toString().replace("-", "") + suffix;

            // 确保 bucket 存在
            String bucket = minioConfig.getBucketName();
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }

            // 上传
            try (InputStream is = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucket)
                                .object(objectName)
                                .stream(is, file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build()
                );
            }

            String url = minioConfig.getEndpoint() + "/" + bucket + "/" + objectName;
            log.info("文件上传成功: {}", url);
            return url;

        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw new BusinessException("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件预签名访问URL (用于私有桶)
     *
     * @param objectName 对象名称 (bucket之后的路径)
     * @param expireSec  过期时间(秒)
     */
    public String getPresignedUrl(String objectName, int expireSec) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object(objectName)
                            .method(Method.GET)
                            .expiry(expireSec, TimeUnit.SECONDS)
                            .build()
            );
        } catch (Exception e) {
            log.error("获取预签名URL失败", e);
            throw new BusinessException("获取文件链接失败");
        }
    }

    /**
     * 删除文件
     *
     * @param objectName 对象名称
     */
    public void delete(String objectName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            log.error("文件删除失败: {}", objectName, e);
        }
    }
}
