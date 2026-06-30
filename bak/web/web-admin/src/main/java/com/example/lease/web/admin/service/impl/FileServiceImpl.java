package com.example.lease.web.admin.service.impl;

import com.example.lease.common.minio.MinioProperties;
import com.example.lease.web.admin.service.FileService;
import io.minio.*;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    MinioProperties properties;

    @Autowired
    MinioClient client;

    @Override
    public String upload(MultipartFile file) throws Exception {

        boolean bucketExists = client.bucketExists(BucketExistsArgs.builder().bucket(properties.getBucketName()).build());
        if (!bucketExists) {
            client.makeBucket(MakeBucketArgs.builder().bucket(properties.getBucketName()).build());
            client.setBucketPolicy(SetBucketPolicyArgs.builder()
                    .bucket(properties.getBucketName()).config(createBucketPolicyConfig(properties.getBucketName())).build());
        }

        String filename = new SimpleDateFormat("yyyyMMdd").format(new Date()) + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

        //   image/jpg   image/png    image/gif          text/plain      application/json     text/html
        client.putObject(PutObjectArgs.builder()
                .bucket(properties.getBucketName())
                .object(filename)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType()).build());
        //           http://192.168.6.101:9000/lease/20250219/sfjasldkfjdsldjfslsdf-meinv.png
        return String.join("/", properties.getEndpoint(), properties.getBucketName(), filename);


    }

    // 自定义桶的访问策略： 允许所有人访问这个桶的所有资源
    private String createBucketPolicyConfig(String bucketName) {
        //jdk13提供特性： 文本块

//        String m = "a" +
//                "b" +
//                "c";
//
//        String s = """
//                abc
//                edf
//                ght
//                """;

        return """
                {
                  "Statement" : [ {
                    "Action" : "s3:GetObject",
                    "Effect" : "Allow",
                    "Principal" : "*",
                    "Resource" : "arn:aws:s3:::%s/*"
                  } ],
                  "Version" : "2012-10-17"
                }
                """.formatted(bucketName); // 给 %s 占位符传参。
    }

}
