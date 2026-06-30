package com.seig.rental.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 短信验证码
 */
@Data
@TableName("sms_code")
public class SmsCode {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 手机号 */
    private String phone;

    /** 验证码 */
    private String code;

    /** 类型: login / register */
    private String type;

    /** 是否已使用: 0=未使用, 1=已使用 */
    private Integer used;

    /** 过期时间 */
    private LocalDateTime expireTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
