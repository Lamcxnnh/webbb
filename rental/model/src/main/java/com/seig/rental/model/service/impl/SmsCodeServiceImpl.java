package com.seig.rental.model.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seig.rental.model.entity.SmsCode;
import com.seig.rental.model.mapper.SmsCodeMapper;
import com.seig.rental.model.service.SmsCodeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class SmsCodeServiceImpl extends ServiceImpl<SmsCodeMapper, SmsCode> implements SmsCodeService {

    @Override
    public String sendCode(String phone, String type) {
        // 生成6位数字验证码
        String code = String.format("%06d", new Random().nextInt(1000000));

        // 保存到数据库（5分钟有效）
        SmsCode smsCode = new SmsCode();
        smsCode.setPhone(phone);
        smsCode.setCode(code);
        smsCode.setType(type);
        smsCode.setUsed(0);
        smsCode.setExpireTime(LocalDateTime.now().plusMinutes(5));
        this.save(smsCode);

        // 开发环境直接返回验证码，生产环境接入短信服务商
        return code;
    }

    @Override
    public boolean verifyCode(String phone, String code, String type) {
        // 查找最新一条未使用且未过期的验证码
        SmsCode smsCode = this.getOne(new LambdaQueryWrapper<SmsCode>()
                .eq(SmsCode::getPhone, phone)
                .eq(SmsCode::getType, type)
                .eq(SmsCode::getUsed, 0)
                .ge(SmsCode::getExpireTime, LocalDateTime.now())
                .orderByDesc(SmsCode::getCreateTime)
                .last("LIMIT 1"));

        if (smsCode == null || !smsCode.getCode().equals(code)) {
            return false;
        }

        // 标记为已使用
        smsCode.setUsed(1);
        this.updateById(smsCode);
        return true;
    }
}
