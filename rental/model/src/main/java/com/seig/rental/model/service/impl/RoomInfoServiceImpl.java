package com.seig.rental.model.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seig.rental.model.entity.*;
import com.seig.rental.model.mapper.*;
import com.seig.rental.model.service.RoomInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class RoomInfoServiceImpl extends ServiceImpl<RoomInfoMapper, RoomInfo> implements RoomInfoService {

    @Resource
    private RoomPaymentMapper roomPaymentMapper;
    @Resource
    private RoomLeaseTermMapper roomLeaseTermMapper;
    @Resource
    private RoomLabelMapper roomLabelMapper;
    @Resource
    private RoomFacilityMapper roomFacilityMapper;

    @Override
    @Transactional
    public void saveOrUpdateRoom(RoomInfo roomInfo,
                                  List<Long> paymentTypeIds,
                                  List<Long> leaseTermIds,
                                  List<Long> labelIds,
                                  List<Long> facilityIds) {
        // 1. 保存或更新房间基本信息
        this.saveOrUpdate(roomInfo);
        Long roomId = roomInfo.getId();

        // 2. 删除旧的关联数据
        roomPaymentMapper.delete(new LambdaQueryWrapper<RoomPayment>().eq(RoomPayment::getRoomId, roomId));
        roomLeaseTermMapper.delete(new LambdaQueryWrapper<RoomLeaseTerm>().eq(RoomLeaseTerm::getRoomId, roomId));
        roomLabelMapper.delete(new LambdaQueryWrapper<RoomLabel>().eq(RoomLabel::getRoomId, roomId));
        roomFacilityMapper.delete(new LambdaQueryWrapper<RoomFacility>().eq(RoomFacility::getRoomId, roomId));

        // 3. 保存新的关联数据
        if (paymentTypeIds != null) {
            for (Long ptId : paymentTypeIds) {
                RoomPayment rp = new RoomPayment();
                rp.setRoomId(roomId);
                rp.setPaymentTypeId(ptId);
                roomPaymentMapper.insert(rp);
            }
        }
        if (leaseTermIds != null) {
            for (Long ltId : leaseTermIds) {
                RoomLeaseTerm rlt = new RoomLeaseTerm();
                rlt.setRoomId(roomId);
                rlt.setLeaseTermId(ltId);
                roomLeaseTermMapper.insert(rlt);
            }
        }
        if (labelIds != null) {
            for (Long lId : labelIds) {
                RoomLabel rl = new RoomLabel();
                rl.setRoomId(roomId);
                rl.setLabelId(lId);
                roomLabelMapper.insert(rl);
            }
        }
        if (facilityIds != null) {
            for (Long fId : facilityIds) {
                RoomFacility rf = new RoomFacility();
                rf.setRoomId(roomId);
                rf.setFacilityId(fId);
                roomFacilityMapper.insert(rf);
            }
        }
    }

    @Override
    public IPage<RoomInfo> pageQuery(IPage<RoomInfo> page, Long apartmentId, String name, Integer isRelease) {
        LambdaQueryWrapper<RoomInfo> wrapper = new LambdaQueryWrapper<>();
        if (apartmentId != null) {
            wrapper.eq(RoomInfo::getApartmentId, apartmentId);
        }
        if (StringUtils.hasText(name)) {
            wrapper.like(RoomInfo::getName, name);
        }
        if (isRelease != null) {
            wrapper.eq(RoomInfo::getIsRelease, isRelease);
        }
        wrapper.orderByDesc(RoomInfo::getSortOrder)
               .orderByDesc(RoomInfo::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public void updateReleaseStatus(Long id, Integer isRelease) {
        RoomInfo room = new RoomInfo();
        room.setId(id);
        room.setIsRelease(isRelease);
        this.updateById(room);
    }

    @Override
    public List<RoomInfo> listByApartmentId(Long apartmentId) {
        return this.list(new LambdaQueryWrapper<RoomInfo>()
                .eq(RoomInfo::getApartmentId, apartmentId)
                .eq(RoomInfo::getIsRelease, 1)
                .orderByDesc(RoomInfo::getSortOrder));
    }
}
