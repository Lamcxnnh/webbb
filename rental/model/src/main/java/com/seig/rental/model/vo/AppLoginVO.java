package com.seig.rental.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 移动端登录返回
 */
@Data
@AllArgsConstructor
public class AppLoginVO {

    /** 是否新注册用户 */
    private Boolean isNewUser;

    /** 登录token */
    private String token;
}
