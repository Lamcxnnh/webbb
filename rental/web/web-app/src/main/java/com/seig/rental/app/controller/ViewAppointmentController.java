package com.seig.rental.app.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seig.rental.common.base.Result;
import com.seig.rental.common.base.ResultCode;
import com.seig.rental.common.exception.BusinessException;
import com.seig.rental.model.entity.ViewAppointment;
import com.seig.rental.model.service.ViewAppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@Tag(name = "移动端-看房预约")
@RestController
@RequestMapping("/appointment")
public class ViewAppointmentController {

    @Resource
    private ViewAppointmentService viewAppointmentService;

    @Operation(summary = "保存或更新看房预约")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody ViewAppointment appointment) {
        // 设置当前登录用户
        appointment.setUserId(StpUtil.getLoginIdAsLong());
        viewAppointmentService.saveOrUpdate(appointment);
        return Result.ok();
    }

    @Operation(summary = "查询个人预约看房列表")
    @GetMapping("/my")
    public Result<IPage<ViewAppointment>> myList(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size) {
        long userId = StpUtil.getLoginIdAsLong();
        return Result.ok(viewAppointmentService.page(new Page<>(page, size),
                new LambdaQueryWrapper<ViewAppointment>()
                        .eq(ViewAppointment::getUserId, userId)
                        .orderByDesc(ViewAppointment::getCreateTime)));
    }

    @Operation(summary = "根据ID查询预约详情")
    @GetMapping("/{id}")
    public Result<ViewAppointment> getById(@PathVariable Long id) {
        ViewAppointment appointment = viewAppointmentService.getById(id);
        if (appointment == null) {
            throw new BusinessException(ResultCode.APPOINTMENT_NOT_EXIST);
        }
        return Result.ok(appointment);
    }
}
