package com.seig.rental.app.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.seig.rental.common.base.Result;
import com.seig.rental.common.base.ResultCode;
import com.seig.rental.common.exception.BusinessException;
import com.seig.rental.model.dto.AppLoginDTO;
import com.seig.rental.model.entity.UserInfo;
import com.seig.rental.model.service.SmsCodeService;
import com.seig.rental.model.service.UserInfoService;
import com.seig.rental.model.vo.AppLoginVO;
import com.seig.rental.model.vo.AppUserInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 移动端-登录管理
 */
@Slf4j
@Tag(name = "移动端-登录管理")
@RestController
@RequestMapping("/login")
public class LoginController {

    @Resource
    private SmsCodeService smsCodeService;

    @Resource
    private UserInfoService userInfoService;

    /**
     * 获取短信验证码
     */
    @Operation(summary = "获取短信验证码")
    @GetMapping("/sms-code")
    public Result<String> sendSmsCode(@RequestParam String phone) {
        String code = smsCodeService.sendCode(phone, "login");
        log.info("手机号 {} 的验证码: {}", phone, code);
        // 开发环境返回验证码，生产环境应改为："验证码已发送"
        return Result.ok("验证码: " + code + "（5分钟有效）");
    }

    /**
     * 登录/注册（验证码登录）
     */
    @Operation(summary = "登录/注册（验证码登录）")
    @PostMapping
    public Result<AppLoginVO> login(@Valid @RequestBody AppLoginDTO dto) {
        // 1. 校验验证码
        if (!smsCodeService.verifyCode(dto.getPhone(), dto.getCode(), "login")) {
            throw new BusinessException(ResultCode.VERIFY_CODE_ERROR);
        }

        // 2. 查询或创建用户
        UserInfo user = userInfoService.getByPhone(dto.getPhone());
        boolean isNewUser = (user == null);

        if (isNewUser) {
            user = new UserInfo();
            user.setPhone(dto.getPhone());
            user.setNickname("用户" + dto.getPhone().substring(Math.max(0, dto.getPhone().length() - 4)));
            user.setStatus(1);
            userInfoService.save(user);
        } else if (user.getStatus() == 0) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }

        // 3. Sa-Token 登录
        StpUtil.login(user.getId());
        String token = StpUtil.getTokenValue();

        // 4. 更新最后登录时间
        UserInfo updateUser = new UserInfo();
        updateUser.setId(user.getId());
        updateUser.setLastLoginTime(LocalDateTime.now());
        userInfoService.updateById(updateUser);

        log.info("移动端用户登录: id={}, phone={}, 新用户={}", user.getId(), dto.getPhone(), isNewUser);
        return Result.ok(new AppLoginVO(isNewUser, token));
    }

    /**
     * 获取当前登录用户信息
     */
    @Operation(summary = "获取当前登录用户信息")
    @GetMapping("/info")
    public Result<AppUserInfoVO> info() {
        long userId = StpUtil.getLoginIdAsLong();
        UserInfo user = userInfoService.getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }

        AppUserInfoVO vo = new AppUserInfoVO();
        vo.setId(user.getId());
        vo.setPhone(user.getPhone());
        vo.setNickname(user.getNickname());
        vo.setAvatarUrl(user.getAvatarUrl());
        vo.setRealName(user.getRealName());
        vo.setIdCard(user.getIdCard());
        vo.setStatus(user.getStatus());
        vo.setLastLoginTime(user.getLastLoginTime());
        return Result.ok(vo);
    }
}
