package com.example.lease.web.admin.custom.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.lease.model.entity.ViewAppointment;
import com.example.lease.model.enums.AppointmentStatus;
import com.example.lease.web.admin.service.ViewAppointmentService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 过期预约自动取消任务
 *
 * @author lease
 */
@Component
@Slf4j
public class AppointmentCleanupJobHandler {

    @Autowired
    private ViewAppointmentService viewAppointmentService;

    /**
     * 将超过3天未处理的待看房预约自动取消
     * 建议 CRON: 0 0 3 * * ? (每天凌晨3点执行)
     */
    @XxlJob("appointmentCleanupHandler")
    public void handleAppointmentCleanup() {
        log.info("[XXL-JOB] 开始执行过期预约自动取消任务");
        XxlJobHelper.log("开始扫描过期的待看房预约...");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -3);
        Date threeDaysAgo = calendar.getTime();

        LambdaQueryWrapper<ViewAppointment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ViewAppointment::getAppointmentStatus, AppointmentStatus.WAITING)
                .lt(ViewAppointment::getAppointmentTime, threeDaysAgo);

        List<ViewAppointment> staleList = viewAppointmentService.list(queryWrapper);

        if (staleList == null || staleList.isEmpty()) {
            log.info("[XXL-JOB] 未发现过期的待看房预约");
            XxlJobHelper.log("未发现过期的待看房预约");
            XxlJobHelper.handleSuccess();
            return;
        }

        log.info("[XXL-JOB] 发现 {} 条过期预约，开始自动取消", staleList.size());
        XxlJobHelper.log("发现 " + staleList.size() + " 条过期预约");

        staleList.forEach(appointment -> appointment.setAppointmentStatus(AppointmentStatus.CANCELED));
        boolean success = viewAppointmentService.updateBatchById(staleList);

        if (success) {
            log.info("[XXL-JOB] 过期预约清理完成，共取消 {} 条预约", staleList.size());
            XxlJobHelper.log("过期预约清理完成，共取消 " + staleList.size() + " 条预约");
            XxlJobHelper.handleSuccess();
        } else {
            log.error("[XXL-JOB] 过期预约清理失败");
            XxlJobHelper.handleFail("批量取消预约失败");
        }
    }
}
