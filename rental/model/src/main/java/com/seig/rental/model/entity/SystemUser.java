package com.seig.rental.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 后台管理员用户
 */
@Data
@TableName("system_user")
public class SystemUser {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户名 */
    private String username;

    /** 密码 (加密) */
    private String password;

    /** 姓名 */
    private String name;

    /** 头像URL */
    private String avatarUrl;

    /** 手机号 */
    private String phone;

    /** 类型: super=超级管理员, admin=普通管理员 */
    private String type;

    /** 状态: 0=禁用, 1=启用 */
    private Integer status;

    /** 最后登录时间 */
    private LocalDateTime lastLoginTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
