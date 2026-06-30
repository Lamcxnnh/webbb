package com.seig.rental.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seig.rental.common.base.Result;
import com.seig.rental.model.entity.ApartmentInfo;
import com.seig.rental.model.service.ApartmentInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "后台-公寓管理")
@RestController
@RequestMapping("/apartment")
public class ApartmentController {

    @Resource
    private ApartmentInfoService apartmentInfoService;

    @Operation(summary = "保存或更新公寓信息")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody ApartmentInfo apartmentInfo) {
        apartmentInfoService.saveOrUpdate(apartmentInfo);
        return Result.ok();
    }

    @Operation(summary = "根据条件分页查询公寓列表")
    @GetMapping("/page")
    public Result<IPage<ApartmentInfo>> page(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long districtId,
            @RequestParam(required = false) Integer isRelease) {
        IPage<ApartmentInfo> result = apartmentInfoService.pageQuery(
                new Page<>(page, size), name, districtId, isRelease);
        return Result.ok(result);
    }

    @Operation(summary = "根据ID获取公寓信息")
    @GetMapping("/{id}")
    public Result<ApartmentInfo> getById(@PathVariable Long id) {
        return Result.ok(apartmentInfoService.getById(id));
    }

    @Operation(summary = "根据ID删除公寓信息")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        apartmentInfoService.removeById(id);
        return Result.ok();
    }

    @Operation(summary = "根据ID修改公寓发布状态")
    @PutMapping("/{id}/release")
    public Result<Void> updateRelease(@PathVariable Long id, @RequestParam Integer isRelease) {
        apartmentInfoService.updateReleaseStatus(id, isRelease);
        return Result.ok();
    }

    @Operation(summary = "根据区县ID查询公寓列表")
    @GetMapping("/list-by-district")
    public Result<List<ApartmentInfo>> listByDistrict(@RequestParam Long districtId) {
        List<ApartmentInfo> list = apartmentInfoService.list(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ApartmentInfo>()
                        .eq(ApartmentInfo::getDistrictId, districtId)
                        .eq(ApartmentInfo::getIsRelease, 1)
                        .orderByDesc(ApartmentInfo::getSortOrder));
        return Result.ok(list);
    }
}
