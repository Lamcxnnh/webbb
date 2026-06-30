package com.seig.rental.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 管理员信息返回
 */
@Data
public class AdminInfoVO {

    private Long id;
    private String username;
    private String name;
    private String avatarUrl;
    private String phone;
    private String type;
    private LocalDateTime lastLoginTime;
}
