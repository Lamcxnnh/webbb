package com.seig.rental.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seig.rental.common.base.Result;
import com.seig.rental.model.dto.RoomSaveDTO;
import com.seig.rental.model.entity.RoomInfo;
import com.seig.rental.model.service.RoomInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "后台-房间管理")
@RestController
@RequestMapping("/room")
public class RoomController {

    @Resource
    private RoomInfoService roomInfoService;

    @Operation(summary = "保存或更新房间信息（含关联表）")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody RoomSaveDTO dto) {
        RoomInfo roomInfo = new RoomInfo();
        BeanUtils.copyProperties(dto, roomInfo);
        roomInfoService.saveOrUpdateRoom(
                roomInfo,
                dto.getPaymentTypeIds(),
                dto.getLeaseTermIds(),
                dto.getLabelIds(),
                dto.getFacilityIds());
        return Result.ok();
    }

    @Operation(summary = "根据条件分页查询房间列表")
    @GetMapping("/page")
    public Result<IPage<RoomInfo>> page(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) Long apartmentId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer isRelease) {
        IPage<RoomInfo> result = roomInfoService.pageQuery(
                new Page<>(page, size), apartmentId, name, isRelease);
        return Result.ok(result);
    }

    @Operation(summary = "根据ID获取房间信息")
    @GetMapping("/{id}")
    public Result<RoomInfo> getById(@PathVariable Long id) {
        return Result.ok(roomInfoService.getById(id));
    }

    @Operation(summary = "根据ID删除房间信息")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        roomInfoService.removeById(id);
        return Result.ok();
    }

    @Operation(summary = "根据ID修改房间发布状态")
    @PutMapping("/{id}/release")
    public Result<Void> updateRelease(@PathVariable Long id, @RequestParam Integer isRelease) {
        roomInfoService.updateReleaseStatus(id, isRelease);
        return Result.ok();
    }

    @Operation(summary = "根据公寓ID查询房间列表")
    @GetMapping("/list-by-apartment")
    public Result<List<RoomInfo>> listByApartment(@RequestParam Long apartmentId) {
        return Result.ok(roomInfoService.listByApartmentId(apartmentId));
    }
}
