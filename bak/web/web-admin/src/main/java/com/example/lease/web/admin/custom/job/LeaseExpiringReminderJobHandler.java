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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 即将到期租约提醒任务
 *
 * @author lease
 */
@Component
@Slf4j
public class LeaseExpiringReminderJobHandler {

    @Autowired
    private LeaseAgreementService leaseAgreementService;

    /**
     * 查询未来7天内到期的已签约租约，提前记录提醒
     * 建议 CRON: 0 0 9 * * ? (每天上午9点执行)
     */
    @XxlJob("leaseExpiringReminderHandler")
    public void handleExpiringReminder() {
        log.info("[XXL-JOB] 开始执行即将到期租约提醒任务");
        XxlJobHelper.log("开始扫描即将到期的租约...");

        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        Date sevenDaysLater = calendar.getTime();

        LambdaQueryWrapper<LeaseAgreement> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LeaseAgreement::getStatus, LeaseStatus.SIGNED)
                .ge(LeaseAgreement::getLeaseEndDate, now)
                .le(LeaseAgreement::getLeaseEndDate, sevenDaysLater);

        List<LeaseAgreement> expiringList = leaseAgreementService.list(queryWrapper);

        if (expiringList == null || expiringList.isEmpty()) {
            log.info("[XXL-JOB] 未来7天内无即将到期的租约");
            XxlJobHelper.log("未来7天内无即将到期的租约");
            XxlJobHelper.handleSuccess();
            return;
        }

        log.info("[XXL-JOB] 发现 {} 条即将到期的租约", expiringList.size());
        XxlJobHelper.log("发现 " + expiringList.size() + " 条即将到期的租约");

        for (LeaseAgreement agreement : expiringList) {
            String msg = String.format("租约[ID=%d] 承租人[%s] 将于 %s 到期，请及时处理",
                    agreement.getId(), agreement.getName(), agreement.getLeaseEndDate());
            log.warn("[XXL-JOB] {}", msg);
            XxlJobHelper.log(msg);
        }

        XxlJobHelper.handleSuccess("即将到期租约提醒完成，共 " + expiringList.size() + " 条");
    }
}
