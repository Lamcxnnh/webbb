package com.seig.rental.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 图形验证码返回
 */
@Data
@AllArgsConstructor
public class CaptchaVO {

    /** 验证码key (登录时回传) */
    private String captchaKey;

    /** Base64编码的验证码图片 */
    private String captchaImage;
}
