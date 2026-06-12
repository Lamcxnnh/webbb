package com.example.lease.web.admin.service.impl;

import com.example.lease.model.entity.SysLog;
import com.example.lease.web.admin.mapper.SysLogMapper;
import com.example.lease.web.admin.service.AsyncLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 异步日志保存服务实现
 * <p>
 * 使用 @Async 注解确保日志入库操作在独立线程中执行，
 * 不阻塞主业务线程
 */
@Slf4j
@Service
public class AsyncLogServiceImpl implements AsyncLogService {

    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    @Async("logTaskExecutor")
    public void saveLog(SysLog sysLog) {
        try {
            sysLogMapper.insert(sysLog);
            log.debug("操作日志已保存: {}", sysLog.getOperation());
        } catch (Exception e) {
            log.error("操作日志入库失败: {}", e.getMessage(), e);
        }
    }
}
