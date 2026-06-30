package com.example.lease.web.admin.custom.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.lease.model.entity.LeaseAgreement;
import com.example.lease.model.enums.LeaseStatus;
import com.example.lease.web.admin.service.LeaseAgreementService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 租约到期自动处理任务
 *
 * @author lease
 */
@Component
@Slf4j
public class LeaseExpirationJobHandler {

    @Autowired
    private LeaseAgreementService leaseAgreementService;

    /**
     * 扫描所有已签约但已到期的租约，将状态更新为"已到期"
     * 建议 CRON: 0 0 2 * * ? (每天凌晨2点执行)
     */
    @XxlJob("leaseExpirationHandler")
    public void handleLeaseExpiration() {
        log.info("[XXL-JOB] 开始执行租约到期自动处理任务");
        XxlJobHelper.log("开始扫描到期的已签约租约...");

        LambdaQueryWrapper<LeaseAgreement> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LeaseAgreement::getStatus, LeaseStatus.SIGNED)
                .lt(LeaseAgreement::getLeaseEndDate, new Date());

        List<LeaseAgreement> expiredList = leaseAgreementService.list(queryWrapper);

        if (expiredList == null || expiredList.isEmpty()) {
            log.info("[XXL-JOB] 未发现到期的租约，任务结束");
            XxlJobHelper.log("未发现到期的租约");
            XxlJobHelper.handleSuccess();
            return;
        }

        log.info("[XXL-JOB] 发现 {} 条已到期的租约，开始更新状态", expiredList.size());
        XxlJobHelper.log("发现 " + expiredList.size() + " 条已到期的租约");

        expiredList.forEach(agreement -> agreement.setStatus(LeaseStatus.EXPIRED));
        boolean success = leaseAgreementService.updateBatchById(expiredList);

        if (success) {
            log.info("[XXL-JOB] 租约到期处理完成，共处理 {} 条记录", expiredList.size());
            XxlJobHelper.log("租约到期处理完成，共处理 " + expiredList.size() + " 条记录");
            XxlJobHelper.handleSuccess();
        } else {
            log.error("[XXL-JOB] 租约到期处理失败");
            XxlJobHelper.handleFail("批量更新租约状态失败");
        }
    }
}
