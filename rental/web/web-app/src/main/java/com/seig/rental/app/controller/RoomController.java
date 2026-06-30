package com.seig.rental.app.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seig.rental.common.base.Result;
import com.seig.rental.common.base.ResultCode;
import com.seig.rental.common.exception.BusinessException;
import com.seig.rental.model.entity.*;
import com.seig.rental.model.mapper.*;
import com.seig.rental.model.service.RoomInfoService;
import com.seig.rental.model.vo.RoomDetailVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "移动端-房间信息")
@RestController
@RequestMapping("/room")
public class RoomController {

    @Resource
    private RoomInfoService roomInfoService;
    @Resource
    private RoomPaymentMapper roomPaymentMapper;
    @Resource
    private RoomLeaseTermMapper roomLeaseTermMapper;
    @Resource
    private RoomLabelMapper roomLabelMapper;
    @Resource
    private RoomFacilityMapper roomFacilityMapper;
    @Resource
    private PaymentTypeMapper paymentTypeMapper;
    @Resource
    private LeaseTermMapper leaseTermMapper;
    @Resource
    private LabelInfoMapper labelInfoMapper;
    @Resource
    private FacilityInfoMapper facilityInfoMapper;
    @Resource
    private ApartmentInfoMapper apartmentInfoMapper;

    @Operation(summary = "根据条件分页查询房间列表")
    @GetMapping("/page")
    public Result<IPage<RoomInfo>> page(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) Long apartmentId,
            @RequestParam(required = false) Long districtId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {

        LambdaQueryWrapper<RoomInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoomInfo::getIsRelease, 1); // 只查已发布

        if (apartmentId != null) {
            wrapper.eq(RoomInfo::getApartmentId, apartmentId);
        }
        if (StrUtil.isNotBlank(name)) {
            wrapper.like(RoomInfo::getName, name);
        }
        if (minPrice != null) {
            wrapper.ge(RoomInfo::getPrice, minPrice);
        }
        if (maxPrice != null) {
            wrapper.le(RoomInfo::getPrice, maxPrice);
        }
        if (districtId != null) {
            // 查询该区县下的公寓ID列表
            List<ApartmentInfo> apartments = apartmentInfoMapper.selectList(
                    new LambdaQueryWrapper<ApartmentInfo>()
                            .eq(ApartmentInfo::getDistrictId, districtId)
                            .eq(ApartmentInfo::getIsRelease, 1));
            List<Long> aptIds = apartments.stream().map(ApartmentInfo::getId).collect(Collectors.toList());
            if (CollUtil.isEmpty(aptIds)) {
                return Result.ok(new Page<>(page, size));
            }
            wrapper.in(RoomInfo::getApartmentId, aptIds);
        }
        wrapper.orderByDesc(RoomInfo::getSortOrder).orderByDesc(RoomInfo::getCreateTime);
        return Result.ok(roomInfoService.page(new Page<>(page, size), wrapper));
    }

    @Operation(summary = "根据ID查询房间详细信息")
    @GetMapping("/{id}")
    public Result<RoomDetailVO> detail(@PathVariable Long id) {
        RoomInfo room = roomInfoService.getById(id);
        if (room == null) {
            throw new BusinessException(ResultCode.ROOM_NOT_EXIST);
        }

        RoomDetailVO vo = new RoomDetailVO();
        // 复制房间基本信息
        vo.setId(room.getId());
        vo.setApartmentId(room.getApartmentId());
        vo.setName(room.getName());
        vo.setIntro(room.getIntro());
        vo.setPrice(room.getPrice());
        vo.setArea(room.getArea());
        vo.setFloor(room.getFloor());
        vo.setRoomType(room.getRoomType());
        vo.setCoverUrl(room.getCoverUrl());
        vo.setImages(parseJsonArray(room.getImages()));
        vo.setAttrValueIds(parseLongList(room.getAttrValueIds()));
        vo.setIsRelease(room.getIsRelease());
        vo.setSortOrder(room.getSortOrder());
        vo.setCreateTime(room.getCreateTime());
        vo.setUpdateTime(room.getUpdateTime());

        // 关联数据
        vo.setPaymentTypes(getPaymentTypesByRoomId(id));
        vo.setLeaseTerms(getLeaseTermsByRoomId(id));
        vo.setLabels(getLabelsByRoomId(id));
        vo.setFacilities(getFacilitiesByRoomId(id));

        // 公寓简要信息
        ApartmentInfo apartment = apartmentInfoMapper.selectById(room.getApartmentId());
        if (apartment != null) {
            vo.setApartmentName(apartment.getName());
            vo.setApartmentCoverUrl(apartment.getCoverUrl());
        }

        return Result.ok(vo);
    }

    @Operation(summary = "根据公寓ID分页查询房间列表")
    @GetMapping("/page-by-apartment")
    public Result<IPage<RoomInfo>> pageByApartment(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam Long apartmentId) {
        return Result.ok(roomInfoService.page(new Page<>(page, size),
                new LambdaQueryWrapper<RoomInfo>()
                        .eq(RoomInfo::getApartmentId, apartmentId)
                        .eq(RoomInfo::getIsRelease, 1)
                        .orderByDesc(RoomInfo::getSortOrder)));
    }

    // ===== 关联查询辅助方法 =====

    private List<PaymentType> getPaymentTypesByRoomId(Long roomId) {
        List<RoomPayment> rps = roomPaymentMapper.selectList(
                new LambdaQueryWrapper<RoomPayment>().eq(RoomPayment::getRoomId, roomId));
        if (CollUtil.isEmpty(rps)) return Collections.emptyList();
        List<Long> ids = rps.stream().map(RoomPayment::getPaymentTypeId).collect(Collectors.toList());
        return paymentTypeMapper.selectBatchIds(ids);
    }

    private List<LeaseTerm> getLeaseTermsByRoomId(Long roomId) {
        List<RoomLeaseTerm> rlts = roomLeaseTermMapper.selectList(
                new LambdaQueryWrapper<RoomLeaseTerm>().eq(RoomLeaseTerm::getRoomId, roomId));
        if (CollUtil.isEmpty(rlts)) return Collections.emptyList();
        List<Long> ids = rlts.stream().map(RoomLeaseTerm::getLeaseTermId).collect(Collectors.toList());
        return leaseTermMapper.selectBatchIds(ids);
    }

    private List<LabelInfo> getLabelsByRoomId(Long roomId) {
        List<RoomLabel> rls = roomLabelMapper.selectList(
                new LambdaQueryWrapper<RoomLabel>().eq(RoomLabel::getRoomId, roomId));
        if (CollUtil.isEmpty(rls)) return Collections.emptyList();
        List<Long> ids = rls.stream().map(RoomLabel::getLabelId).collect(Collectors.toList());
        return labelInfoMapper.selectBatchIds(ids);
    }

    private List<FacilityInfo> getFacilitiesByRoomId(Long roomId) {
        List<RoomFacility> rfs = roomFacilityMapper.selectList(
                new LambdaQueryWrapper<RoomFacility>().eq(RoomFacility::getRoomId, roomId));
        if (CollUtil.isEmpty(rfs)) return Collections.emptyList();
        List<Long> ids = rfs.stream().map(RoomFacility::getFacilityId).collect(Collectors.toList());
        return facilityInfoMapper.selectBatchIds(ids);
    }

    @SuppressWarnings("unchecked")
    private List<String> parseJsonArray(String json) {
        if (StrUtil.isBlank(json)) return Collections.emptyList();
        try {
            return cn.hutool.json.JSONUtil.toList(json, String.class);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private List<Long> parseLongList(String json) {
        if (StrUtil.isBlank(json)) return Collections.emptyList();
        try {
            return cn.hutool.json.JSONUtil.toList(json, Long.class);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
