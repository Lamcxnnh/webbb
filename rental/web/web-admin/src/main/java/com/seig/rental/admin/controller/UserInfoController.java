package com.seig.rental.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seig.rental.common.base.Result;
import com.seig.rental.model.entity.UserInfo;
import com.seig.rental.model.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@Tag(name = "后台-用户管理")
@RestController
@RequestMapping("/user")
public class UserInfoController {

    @Resource
    private UserInfoService userInfoService;

    @Operation(summary = "根据条件分页查询用户列表")
    @GetMapping("/page")
    public Result<IPage<UserInfo>> page(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) Integer status) {
        return Result.ok(userInfoService.pageQuery(new Page<>(page, size), phone, nickname, status));
    }

    @Operation(summary = "根据ID更新用户状态（启用/禁用）")
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        userInfoService.updateStatus(id, status);
        return Result.ok();
    }
}
