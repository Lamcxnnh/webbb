package com.example.lease.web.admin.service;

import com.example.lease.web.admin.vo.login.CaptchaVo;
import com.example.lease.web.admin.vo.login.LoginVo;

public interface LoginService {

    CaptchaVo getCaptcha();

    String login(LoginVo loginVo);

}
