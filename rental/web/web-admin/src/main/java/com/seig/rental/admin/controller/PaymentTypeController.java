package com.seig.rental.admin.controller;

import com.seig.rental.common.base.Result;
import com.seig.rental.model.entity.PaymentType;
import com.seig.rental.model.service.PaymentTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "后台-支付方式管理")
@RestController
@RequestMapping("/payment-type")
public class PaymentTypeController {

    @Resource
    private PaymentTypeService paymentTypeService;

    @Operation(summary = "查询全部支付方式")
    @GetMapping("/list")
    public Result<List<PaymentType>> list() {
        return Result.ok(paymentTypeService.list());
    }

    @Operation(summary = "保存或更新支付方式")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody PaymentType paymentType) {
        paymentTypeService.saveOrUpdate(paymentType);
        return Result.ok();
    }

    @Operation(summary = "根据ID删除支付方式")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        paymentTypeService.removeById(id);
        return Result.ok();
    }
}
