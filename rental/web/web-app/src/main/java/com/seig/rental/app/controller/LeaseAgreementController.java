package com.seig.rental.app.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seig.rental.common.base.Result;
import com.seig.rental.common.base.ResultCode;
import com.seig.rental.common.exception.BusinessException;
import com.seig.rental.model.dto.LeaseAgreementSaveDTO;
import com.seig.rental.model.entity.*;
import com.seig.rental.model.mapper.LeaseTermMapper;
import com.seig.rental.model.mapper.PaymentTypeMapper;
import com.seig.rental.model.mapper.RoomLeaseTermMapper;
import com.seig.rental.model.mapper.RoomPaymentMapper;
import com.seig.rental.model.service.LeaseAgreementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "移动端-租约管理")
@RestController
@RequestMapping("/lease")
public class LeaseAgreementController {

    @Resource
    private LeaseAgreementService leaseAgreementService;
    @Resource
    private RoomPaymentMapper roomPaymentMapper;
    @Resource
    private RoomLeaseTermMapper roomLeaseTermMapper;
    @Resource
    private PaymentTypeMapper paymentTypeMapper;
    @Resource
    private LeaseTermMapper leaseTermMapper;

    @Operation(summary = "获取个人租约列表")
    @GetMapping("/my")
    public Result<IPage<LeaseAgreement>> myList(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size) {
        long userId = StpUtil.getLoginIdAsLong();
        return Result.ok(leaseAgreementService.page(new Page<>(page, size),
                new LambdaQueryWrapper<LeaseAgreement>()
                        .eq(LeaseAgreement::getUserId, userId)
                        .orderByDesc(LeaseAgreement::getCreateTime)));
    }

    @Operation(summary = "根据ID获取租约详细信息")
    @GetMapping("/{id}")
    public Result<LeaseAgreement> getById(@PathVariable Long id) {
        LeaseAgreement agreement = leaseAgreementService.getById(id);
        if (agreement == null) {
            throw new BusinessException(ResultCode.LEASE_NOT_EXIST);
        }
        return Result.ok(agreement);
    }

    @Operation(summary = "保存或更新租约")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody LeaseAgreementSaveDTO dto) {
        LeaseAgreement agreement = new LeaseAgreement();
        BeanUtils.copyProperties(dto, agreement);
        // 设置当前登录用户
        agreement.setUserId(StpUtil.getLoginIdAsLong());
        leaseAgreementService.saveOrUpdate(agreement);
        return Result.ok();
    }

    @Operation(summary = "根据房间ID获取可选支付方式")
    @GetMapping("/payment-options")
    public Result<List<PaymentType>> getPaymentOptions(@RequestParam Long roomId) {
        List<RoomPayment> rps = roomPaymentMapper.selectList(
                new LambdaQueryWrapper<RoomPayment>().eq(RoomPayment::getRoomId, roomId));
        if (rps.isEmpty()) return Result.ok(Collections.emptyList());

        List<Long> ids = rps.stream().map(RoomPayment::getPaymentTypeId).collect(Collectors.toList());
        return Result.ok(paymentTypeMapper.selectBatchIds(ids));
    }

    @Operation(summary = "根据房间ID获取可选租期")
    @GetMapping("/term-options")
    public Result<List<LeaseTerm>> getTermOptions(@RequestParam Long roomId) {
        List<RoomLeaseTerm> rlts = roomLeaseTermMapper.selectList(
                new LambdaQueryWrapper<RoomLeaseTerm>().eq(RoomLeaseTerm::getRoomId, roomId));
        if (rlts.isEmpty()) return Result.ok(Collections.emptyList());

        List<Long> ids = rlts.stream().map(RoomLeaseTerm::getLeaseTermId).collect(Collectors.toList());
        return Result.ok(leaseTermMapper.selectBatchIds(ids));
    }
}
