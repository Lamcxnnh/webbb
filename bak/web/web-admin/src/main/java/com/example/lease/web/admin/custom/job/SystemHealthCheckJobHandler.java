package com.example.lease.web.admin.custom.job;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 系统健康检查任务
 * 用于验证 XXL-JOB 执行器连通性和基本功能
 *
 * @author lease
 */
@Component
@Slf4j
public class SystemHealthCheckJobHandler {

    /**
     * 系统健康检查
     * 建议 CRON: 0 * /5 * * * ? (每5分钟执行)
     */
    @XxlJob("systemHealthCheckHandler")
    public void handleHealthCheck() {
        log.info("[XXL-JOB] 执行器健康检查通过 - {}", LocalDateTime.now());
    }
}
