package com.seig.rental.model.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seig.rental.model.entity.SmsCode;

public interface SmsCodeService extends IService<SmsCode> {

    /**
     * 发送短信验证码（开发环境直接返回验证码）
     * @param phone 手机号
     * @param type  类型: login / register
     * @return 验证码
     */
    String sendCode(String phone, String type);

    /**
     * 校验短信验证码
     * @param phone 手机号
     * @param code  验证码
     * @param type  类型
     * @return true=验证通过
     */
    boolean verifyCode(String phone, String code, String type);
}
