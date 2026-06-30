package com.example.lease.web.admin.service;

import com.example.lease.model.entity.SysLog;

/**
 * 异步日志保存服务接口
 */
public interface AsyncLogService {

    /**
     * 异步保存操作日志到数据库
     *
     * @param sysLog 日志实体
     */
    void saveLog(SysLog sysLog);
}
