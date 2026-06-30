package com.example.lease.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Schema(description = "系统操作日志")
@TableName(value = "sys_log")
@Data
public class SysLog {

    @Schema(description = "日志ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "日志类型")
    @TableField(value = "log_type")
    private Integer logType;

    @Schema(description = "操作描述")
    @TableField(value = "operation")
    private String operation;

    @Schema(description = "执行的方法名")
    @TableField(value = "method_name")
    private String methodName;

    @Schema(description = "请求URL")
    @TableField(value = "request_url")
    private String requestUrl;

    @Schema(description = "请求参数")
    @TableField(value = "request_params")
    private String requestParams;

    @Schema(description = "操作结果")
    @TableField(value = "result")
    private String result;

    @Schema(description = "操作人用户名")
    @TableField(value = "operator")
    private String operator;

    @Schema(description = "客户端IP")
    @TableField(value = "ip_address")
    private String ipAddress;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
}
