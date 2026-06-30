package com.example.lease.web.admin.controller.login;

import com.example.lease.common.annotation.LogSys;
import com.example.lease.common.login.LoginUserHolder;
import com.example.lease.common.result.Result;
import com.example.lease.model.enums.LogTypeEnum;
import com.example.lease.web.admin.service.LoginService;
import com.example.lease.web.admin.vo.login.CaptchaVo;
import com.example.lease.web.admin.vo.login.LoginVo;
import com.example.lease.web.admin.vo.system.user.SystemUserInfoVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "后台登录管理")
@RestController
@RequestMapping("/admin")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @LogSys(value = "获取验证码", logType = LogTypeEnum.DO_LOG)
    @Operation(summary = "验证码")
    @GetMapping("/login/captcha")
    public Result<CaptchaVo> getCaptcha() {
        CaptchaVo captcha = loginService.getCaptcha();
        return Result.ok(captcha);
    }

    @LogSys(value = "后台用户登录", logType = LogTypeEnum.LOGIN)
    @Operation(summary = "登录")
    @PostMapping("login")
    public Result<String> login(@RequestBody LoginVo loginVo) {
        String token = loginService.login(loginVo);
        return Result.ok(token);
    }

    @LogSys(value = "获取登录用户个人信息")
    @Operation(summary = "获取登陆用户个人信息")
    @GetMapping("info")
    public Result<SystemUserInfoVo> info() {
        SystemUserInfoVo userInfo = loginService.getLoginUserInfo(LoginUserHolder.getLoginUser().getUserId());
        return Result.ok(userInfo);
    }
}
