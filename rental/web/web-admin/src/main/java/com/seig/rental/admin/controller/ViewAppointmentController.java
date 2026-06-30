package com.seig.rental.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seig.rental.common.base.Result;
import com.seig.rental.model.entity.ViewAppointment;
import com.seig.rental.model.service.ViewAppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@Tag(name = "后台-看房预约管理")
@RestController
@RequestMapping("/appointment")
public class ViewAppointmentController {

    @Resource
    private ViewAppointmentService viewAppointmentService;

    @Operation(summary = "根据条件分页查询预约信息")
    @GetMapping("/page")
    public Result<IPage<ViewAppointment>> page(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) Long apartmentId,
            @RequestParam(required = false) Long roomId,
            @RequestParam(required = false) String status) {
        return Result.ok(viewAppointmentService.pageQuery(
                new Page<>(page, size), name, phone, apartmentId, roomId, status));
    }

    @Operation(summary = "根据ID更新预约状态")
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id,
                                      @RequestParam String status,
                                      @RequestParam(required = false) String cancelReason) {
        viewAppointmentService.updateStatus(id, status, cancelReason);
        return Result.ok();
    }
}
