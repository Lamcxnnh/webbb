package com.seig.rental.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 移动端登录/注册请求
 */
@Data
public class AppLoginDTO {

    @NotBlank(message = "手机号不能为空")
    private String phone;

    @NotBlank(message = "验证码不能为空")
    private String code;
}
