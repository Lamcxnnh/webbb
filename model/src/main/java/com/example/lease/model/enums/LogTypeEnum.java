package com.example.lease.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 日志类型枚举
 */
@Getter
public enum LogTypeEnum implements BaseEnum {

    /**
     * 系统操作日志
     */
    DO_LOG(1, "操作日志"),

    /**
     * 登录日志
     */
    LOGIN(2, "登录日志"),

    /**
     * 登出日志
     */
    LOGOUT(3, "登出日志");

    @EnumValue
    @JsonValue
    private final Integer code;

    private final String name;

    LogTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
