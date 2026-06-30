package com.example.lease.web.admin.custom.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * XXL-JOB 分布式任务调度配置类
 *
 * @author lease
 */
@Configuration
@Slf4j
public class XxlJobConfiguration {

    @Value("${xxl.job.admin.addresses}")
    private String adminAddresses;

    @Value("${xxl.job.accessToken}")
    private String accessToken;

    @Value("${xxl.job.executor.appname}")
    private String appname;

    @Value("${xxl.job.executor.port:9999}")
    private int port;

    @Value("${xxl.job.executor.logpath:./logs/xxl-job}")
    private String logPath;

    @Value("${xxl.job.executor.logretentiondays:30}")
    private int logRetentionDays;

    @Value("${xxl.job.executor.ip:}")
    private String ip;

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        log.info(">>> XXL-JOB executor initializing, admin={}, appname={}, port={}",
                adminAddresses, appname, port);
        XxlJobSpringExecutor executor = new XxlJobSpringExecutor();
        executor.setAdminAddresses(adminAddresses);
        executor.setAppname(appname);
        executor.setPort(port);
        executor.setLogPath(logPath);
        executor.setLogRetentionDays(logRetentionDays);
        if (ip != null && !ip.isEmpty()) {
            executor.setIp(ip);
        }
        executor.setAccessToken(accessToken);
        return executor;
    }
}
