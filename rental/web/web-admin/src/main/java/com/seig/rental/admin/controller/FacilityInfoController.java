package com.seig.rental.admin.controller;

import com.seig.rental.common.base.Result;
import com.seig.rental.model.entity.FacilityInfo;
import com.seig.rental.model.service.FacilityInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "后台-配套管理")
@RestController
@RequestMapping("/facility")
public class FacilityInfoController {

    @Resource
    private FacilityInfoService facilityInfoService;

    @Operation(summary = "查询全部配套")
    @GetMapping("/list")
    public Result<List<FacilityInfo>> list() {
        return Result.ok(facilityInfoService.list());
    }

    @Operation(summary = "保存或更新配套")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody FacilityInfo facilityInfo) {
        facilityInfoService.saveOrUpdate(facilityInfo);
        return Result.ok();
    }

    @Operation(summary = "根据ID删除配套")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        facilityInfoService.removeById(id);
        return Result.ok();
    }
}
