package com.seig.rental.admin.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.crypto.digest.BCrypt;
import com.seig.rental.common.base.Result;
import com.seig.rental.common.base.ResultCode;
import com.seig.rental.common.exception.BusinessException;
import com.seig.rental.model.dto.LoginDTO;
import com.seig.rental.model.entity.SystemUser;
import com.seig.rental.model.service.SystemUserService;
import com.seig.rental.model.vo.AdminInfoVO;
import com.seig.rental.model.vo.CaptchaVO;
import com.seig.rental.model.vo.LoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 后台登录管理
 */
@Slf4j
@Tag(name = "后台-登录管理")
@RestController
@RequestMapping("/login")
public class LoginController {

    private static final String CAPTCHA_PREFIX = "admin:captcha:";

    @Resource
    private SystemUserService systemUserService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取图形验证码
     */
    @Operation(summary = "获取图形验证码")
    @GetMapping("/captcha")
    public Result<CaptchaVO> captcha() {
        // 生成线段干扰验证码
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(130, 48, 4, 15);
        String code = captcha.getCode();
        String key = UUID.randomUUID().toString().replace("-", "");

        // 存入Redis，5分钟有效
        stringRedisTemplate.opsForValue().set(CAPTCHA_PREFIX + key, code.toLowerCase(), 5, TimeUnit.MINUTES);

        log.info("生成验证码: key={}, code={}", key, code);
        return Result.ok(new CaptchaVO(key, captcha.getImageBase64Data()));
    }

    /**
     * 后台登录
     */
    @Operation(summary = "后台登录")
    @PostMapping
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        // 1. 校验图形验证码
        String redisCode = stringRedisTemplate.opsForValue().get(CAPTCHA_PREFIX + dto.getCaptchaKey());
        if (redisCode == null) {
            throw new BusinessException(ResultCode.CAPTCHA_EXPIRED);
        }
        if (!redisCode.equals(dto.getCaptcha().toLowerCase())) {
            throw new BusinessException(ResultCode.CAPTCHA_ERROR);
        }
        // 验证通过，删除验证码
        stringRedisTemplate.delete(CAPTCHA_PREFIX + dto.getCaptchaKey());

        // 2. 查询管理员
        SystemUser user = systemUserService.getByUsername(dto.getUsername());
        if (user == null) {
            throw new BusinessException(ResultCode.USERNAME_OR_PASSWORD_ERROR);
        }

        // 3. 检查用户状态
        if (user.getStatus() == 0) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }

        // 4. 校验密码 (BCrypt)
        if (!BCrypt.checkpw(dto.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.USERNAME_OR_PASSWORD_ERROR);
        }

        // 5. Sa-Token 登录
        StpUtil.login(user.getId());
        String token = StpUtil.getTokenValue();

        // 6. 更新最后登录时间
        SystemUser updateUser = new SystemUser();
        updateUser.setId(user.getId());
        updateUser.setLastLoginTime(java.time.LocalDateTime.now());
        systemUserService.updateById(updateUser);

        log.info("管理员登录成功: id={}, username={}", user.getId(), user.getUsername());
        return Result.ok(new LoginVO(token));
    }

    /**
     * 获取当前登录用户信息
     */
    @Operation(summary = "获取当前登录用户信息")
    @GetMapping("/info")
    public Result<AdminInfoVO> info() {
        // 从 Sa-Token 获取当前登录用户ID
        long userId = StpUtil.getLoginIdAsLong();

        SystemUser user = systemUserService.getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }

        AdminInfoVO vo = new AdminInfoVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setName(user.getName());
        vo.setAvatarUrl(user.getAvatarUrl());
        vo.setPhone(user.getPhone());
        vo.setType(user.getType());
        vo.setLastLoginTime(user.getLastLoginTime());

        return Result.ok(vo);
    }
}
