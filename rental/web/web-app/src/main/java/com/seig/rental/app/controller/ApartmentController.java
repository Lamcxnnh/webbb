package com.seig.rental.app.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seig.rental.common.base.Result;
import com.seig.rental.common.base.ResultCode;
import com.seig.rental.common.exception.BusinessException;
import com.seig.rental.model.entity.ApartmentInfo;
import com.seig.rental.model.entity.FacilityInfo;
import com.seig.rental.model.mapper.FacilityInfoMapper;
import com.seig.rental.model.service.ApartmentInfoService;
import com.seig.rental.model.vo.ApartmentDetailVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "移动端-公寓信息")
@RestController
@RequestMapping("/apartment")
public class ApartmentController {

    @Resource
    private ApartmentInfoService apartmentInfoService;

    @Resource
    private FacilityInfoMapper facilityInfoMapper;

    @Operation(summary = "根据条件分页查询公寓列表")
    @GetMapping("/page")
    public Result<IPage<ApartmentInfo>> page(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) Long districtId,
            @RequestParam(required = false) String name) {

        LambdaQueryWrapper<ApartmentInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ApartmentInfo::getIsRelease, 1);
        if (districtId != null) {
            wrapper.eq(ApartmentInfo::getDistrictId, districtId);
        }
        if (StrUtil.isNotBlank(name)) {
            wrapper.like(ApartmentInfo::getName, name);
        }
        wrapper.orderByDesc(ApartmentInfo::getSortOrder);
        return Result.ok(apartmentInfoService.page(new Page<>(page, size), wrapper));
    }

    @Operation(summary = "根据ID查询公寓详细信息")
    @GetMapping("/{id}")
    public Result<ApartmentDetailVO> detail(@PathVariable Long id) {
        ApartmentInfo apartment = apartmentInfoService.getById(id);
        if (apartment == null) {
            throw new BusinessException(ResultCode.APARTMENT_NOT_EXIST);
        }

        ApartmentDetailVO vo = new ApartmentDetailVO();
        vo.setId(apartment.getId());
        vo.setName(apartment.getName());
        vo.setIntro(apartment.getIntro());
        vo.setDistrictId(apartment.getDistrictId());
        vo.setDetailAddress(apartment.getDetailAddress());
        vo.setLatitude(apartment.getLatitude());
        vo.setLongitude(apartment.getLongitude());
        vo.setCoverUrl(apartment.getCoverUrl());
        vo.setImages(parseJsonArray(apartment.getImages()));
        vo.setPhone(apartment.getPhone());
        vo.setIsRelease(apartment.getIsRelease());
        vo.setSortOrder(apartment.getSortOrder());
        vo.setCreateTime(apartment.getCreateTime());
        vo.setUpdateTime(apartment.getUpdateTime());

        // 查询配套列表
        vo.setFacilities(getFacilities(apartment.getFacilityIds()));

        return Result.ok(vo);
    }

    @Operation(summary = "根据区县ID查询公寓列表")
    @GetMapping("/list-by-district")
    public Result<List<ApartmentInfo>> listByDistrict(@RequestParam Long districtId) {
        return Result.ok(apartmentInfoService.list(
                new LambdaQueryWrapper<ApartmentInfo>()
                        .eq(ApartmentInfo::getDistrictId, districtId)
                        .eq(ApartmentInfo::getIsRelease, 1)
                        .orderByDesc(ApartmentInfo::getSortOrder)));
    }

    // ===== 辅助方法 =====

    private List<FacilityInfo> getFacilities(String facilityIdsJson) {
        if (StrUtil.isBlank(facilityIdsJson)) return Collections.emptyList();
        try {
            List<Long> ids = cn.hutool.json.JSONUtil.toList(facilityIdsJson, Long.class);
            if (CollUtil.isEmpty(ids)) return Collections.emptyList();
            return facilityInfoMapper.selectBatchIds(ids);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private List<String> parseJsonArray(String json) {
        if (StrUtil.isBlank(json)) return Collections.emptyList();
        try {
            return cn.hutool.json.JSONUtil.toList(json, String.class);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
