package com.seig.rental.app.controller;

import com.seig.rental.common.base.Result;
import com.seig.rental.model.entity.PaymentType;
import com.seig.rental.model.service.PaymentTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "移动端-支付方式")
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
}
