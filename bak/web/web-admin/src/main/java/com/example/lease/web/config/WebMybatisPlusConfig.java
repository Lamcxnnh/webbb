package com.example.lease.web.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.example.lease.web.*.mapper")
public class WebMybatisPlusConfig {
}