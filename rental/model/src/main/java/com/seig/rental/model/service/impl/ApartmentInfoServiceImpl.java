package com.seig.rental.model.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seig.rental.model.entity.ApartmentInfo;
import com.seig.rental.model.mapper.ApartmentInfoMapper;
import com.seig.rental.model.service.ApartmentInfoService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ApartmentInfoServiceImpl extends ServiceImpl<ApartmentInfoMapper, ApartmentInfo> implements ApartmentInfoService {

    @Override
    public IPage<ApartmentInfo> pageQuery(IPage<ApartmentInfo> page, String name, Long districtId, Integer isRelease) {
        LambdaQueryWrapper<ApartmentInfo> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like(ApartmentInfo::getName, name);
        }
        if (districtId != null) {
            wrapper.eq(ApartmentInfo::getDistrictId, districtId);
        }
        if (isRelease != null) {
            wrapper.eq(ApartmentInfo::getIsRelease, isRelease);
        }
        wrapper.orderByDesc(ApartmentInfo::getSortOrder)
               .orderByDesc(ApartmentInfo::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public void updateReleaseStatus(Long id, Integer isRelease) {
        ApartmentInfo apartment = new ApartmentInfo();
        apartment.setId(id);
        apartment.setIsRelease(isRelease);
        this.updateById(apartment);
    }
}
