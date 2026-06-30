package com.seig.rental.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 登录返回
 */
@Data
@AllArgsConstructor
public class LoginVO {

    /** 登录token */
    private String token;
}
