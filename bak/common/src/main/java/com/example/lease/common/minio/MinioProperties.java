package com.example.lease.common.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

//@Component
@Data
@ConfigurationProperties(prefix = "minio") //批量注入属性值
public class MinioProperties {
    //@Value("${minio.endpoint}")
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
}
