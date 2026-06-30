package com.seig.rental.admin.controller;

import com.seig.rental.common.base.Result;
import com.seig.rental.model.entity.DistrictInfo;
import com.seig.rental.model.service.DistrictInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "后台-地区管理")
@RestController
@RequestMapping("/district")
public class DistrictInfoController {

    @Resource
    private DistrictInfoService districtInfoService;

    @Operation(summary = "查询省份列表")
    @GetMapping("/province")
    public Result<List<DistrictInfo>> listProvinces() {
        return Result.ok(districtInfoService.listByLevel("province"));
    }

    @Operation(summary = "根据省份ID查询城市列表")
    @GetMapping("/city")
    public Result<List<DistrictInfo>> listCities(@RequestParam Long parentId) {
        return Result.ok(districtInfoService.listByParentId(parentId));
    }

    @Operation(summary = "根据城市ID查询区县列表")
    @GetMapping("/district")
    public Result<List<DistrictInfo>> listDistricts(@RequestParam Long parentId) {
        return Result.ok(districtInfoService.listByParentId(parentId));
    }
}
