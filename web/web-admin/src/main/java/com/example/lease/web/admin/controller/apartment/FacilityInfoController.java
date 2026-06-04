package com.example.lease.web.admin.controller.apartment;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.lease.common.result.Result;
import com.example.lease.model.entity.FacilityInfo;
import com.example.lease.model.enums.ItemType;
import com.example.lease.web.admin.service.FacilityInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "配套管理")
@RestController
@RequestMapping("/admin/facility")
public class FacilityInfoController {
    @Autowired
    FacilityInfoService facilityInfoService;

    @Operation(summary = "查询配套列表")
    @GetMapping("list")
    public Result<List<FacilityInfo>> list(@RequestParam(required = false) ItemType type) {
        LambdaQueryWrapper<FacilityInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(type!=null,FacilityInfo::getType,type);
        List<FacilityInfo> list = facilityInfoService.list(wrapper);
        return Result.ok(list);
    }

    @Operation(summary = "保存或更改配套")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody FacilityInfo facilityInfo) {
        facilityInfoService.saveOrUpdate(facilityInfo);
        return Result.ok();
    }

    @Operation(summary = "根据Id删除配套")
    @DeleteMapping("deleteById")
    public Result deleteById(@RequestParam Long id) {
        facilityInfoService.removeById(id);
        return Result.ok();
    }
}
