package com.seig.rental.admin.controller;

import com.seig.rental.common.base.Result;
import com.seig.rental.model.entity.LeaseTerm;
import com.seig.rental.model.service.LeaseTermService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "后台-租期管理")
@RestController
@RequestMapping("/lease-term")
public class LeaseTermController {

    @Resource
    private LeaseTermService leaseTermService;

    @Operation(summary = "查询全部租期")
    @GetMapping("/list")
    public Result<List<LeaseTerm>> list() {
        return Result.ok(leaseTermService.list());
    }

    @Operation(summary = "保存或更新租期")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody LeaseTerm leaseTerm) {
        leaseTermService.saveOrUpdate(leaseTerm);
        return Result.ok();
    }

    @Operation(summary = "根据ID删除租期")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        leaseTermService.removeById(id);
        return Result.ok();
    }
}
