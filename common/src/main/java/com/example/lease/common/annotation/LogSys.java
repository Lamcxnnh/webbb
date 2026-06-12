package com.example.lease.common.annotation;

import com.example.lease.model.enums.LogTypeEnum;

import java.lang.annotation.*;

/**
 * 自定义操作日志注解
 * <p>
 * 用于标记需要记录操作日志的方法，配合 AOP 切面实现异步日志记录
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogSys {

    /**
     * 操作描述，例如："查询用户列表"、"新增订单"
     */
    String value() default "";

    /**
     * 日志类型，默认为系统操作日志
     */
    LogTypeEnum logType() default LogTypeEnum.DO_LOG;
}
