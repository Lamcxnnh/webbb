package com.seig.rental.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 移动端用户信息返回
 */
@Data
public class AppUserInfoVO {

    private Long id;
    private String phone;
    private String nickname;
    private String avatarUrl;
    private String realName;
    private String idCard;
    private Integer status;
    private LocalDateTime lastLoginTime;
}
