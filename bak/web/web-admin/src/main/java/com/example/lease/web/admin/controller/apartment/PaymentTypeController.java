package com.example.lease.web.admin.controller.apartment;

import com.example.lease.common.result.Result;
import com.example.lease.model.entity.PaymentType;
import com.example.lease.web.admin.service.PaymentTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="支付方式管理")
@RestController
@RequestMapping("/admin/payment")
public class PaymentTypeController {

    @Autowired
    PaymentTypeService paymentTypeService;

//    查询所有支付方式
    @Operation(summary = "查询所有支付方式")
    @GetMapping("list")
    public Result<List<PaymentType>> listPaymentType() {
        List<PaymentType> paymentTypeList = paymentTypeService.list();
        return Result.ok(paymentTypeList);
    }
//    删除支付方式,根据id
    @Operation(summary = "根据ID删除支付方式")
    @DeleteMapping("deleteById")
    public Result deleteById(@RequestParam Long id){
        paymentTypeService.removeById(id);
        return Result.ok();
    }
//    保存和更新支付方式
    @Operation(summary = "保存或更新支付方式")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody PaymentType paymentType) {
        paymentTypeService.saveOrUpdate(paymentType);
        return Result.ok();
    }
}
