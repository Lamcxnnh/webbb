package com.seig.rental.admin.config;

import com.seig.rental.model.service.LeaseAgreementService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务 - 检查租约状态
 */
@Slf4j
@Component
@EnableScheduling
public class ScheduleTask {

    @Resource
    private LeaseAgreementService leaseAgreementService;

    /**
     * 每天凌晨1点检查到期的租约，自动标记为"已到期"
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void checkLeaseStatus() {
        log.info("开始定时检查租约状态...");
        int count = leaseAgreementService.checkExpiredLeases();
        log.info("租约状态检查完成，{} 条租约已到期", count);
    }
}
