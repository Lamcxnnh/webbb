package com.seig.rental.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seig.rental.common.base.Result;
import com.seig.rental.model.dto.LeaseAgreementSaveDTO;
import com.seig.rental.model.entity.LeaseAgreement;
import com.seig.rental.model.service.LeaseAgreementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@Tag(name = "后台-租约管理")
@RestController
@RequestMapping("/lease")
public class LeaseAgreementController {

    @Resource
    private LeaseAgreementService leaseAgreementService;

    @Operation(summary = "保存或更新租约信息")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody LeaseAgreementSaveDTO dto) {
        LeaseAgreement agreement = new LeaseAgreement();
        BeanUtils.copyProperties(dto, agreement);
        leaseAgreementService.saveOrUpdate(agreement);
        return Result.ok();
    }

    @Operation(summary = "根据条件分页查询租约信息")
    @GetMapping("/page")
    public Result<IPage<LeaseAgreement>> page(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) Long apartmentId,
            @RequestParam(required = false) Long roomId,
            @RequestParam(required = false) String status) {
        return Result.ok(leaseAgreementService.pageQuery(
                new Page<>(page, size), phone, apartmentId, roomId, status));
    }

    @Operation(summary = "根据ID查询租约信息")
    @GetMapping("/{id}")
    public Result<LeaseAgreement> getById(@PathVariable Long id) {
        return Result.ok(leaseAgreementService.getById(id));
    }

    @Operation(summary = "根据ID删除租约信息")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        leaseAgreementService.removeById(id);
        return Result.ok();
    }

    @Operation(summary = "根据ID更新租约状态")
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam String status) {
        leaseAgreementService.updateStatus(id, status);
        return Result.ok();
    }
}
