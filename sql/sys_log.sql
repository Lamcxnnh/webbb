-- ============================================
-- 系统操作日志表
-- 用于存储通过 @LogSys 注解记录的异步操作日志
-- ============================================

CREATE TABLE IF NOT EXISTS `sys_log` (
    `id`            BIGINT          AUTO_INCREMENT  PRIMARY KEY COMMENT '日志ID',
    `log_type`      INT             DEFAULT 1       COMMENT '日志类型 (1:操作日志 2:登录日志 3:登出日志)',
    `operation`     VARCHAR(255)    DEFAULT ''      COMMENT '操作描述',
    `method_name`   VARCHAR(255)    DEFAULT ''      COMMENT '执行的方法全名',
    `request_url`   VARCHAR(255)    DEFAULT ''      COMMENT '请求URL',
    `request_params` TEXT                           COMMENT '请求参数 (JSON格式)',
    `result`        TEXT                            COMMENT '操作返回结果 (JSON格式，异常时记录异常信息)',
    `operator`      VARCHAR(64)     DEFAULT ''      COMMENT '操作人用户名',
    `ip_address`    VARCHAR(64)     DEFAULT ''      COMMENT '客户端IP地址',
    `create_time`   DATETIME                        COMMENT '创建时间',
    INDEX `idx_log_type` (`log_type`),
    INDEX `idx_create_time` (`create_time`),
    INDEX `idx_operator` (`operator`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统操作日志表';
