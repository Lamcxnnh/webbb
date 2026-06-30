package com.seig.rental.model.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seig.rental.model.entity.DistrictInfo;
import com.seig.rental.model.mapper.DistrictInfoMapper;
import com.seig.rental.model.service.DistrictInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictInfoServiceImpl extends ServiceImpl<DistrictInfoMapper, DistrictInfo> implements DistrictInfoService {

    @Override
    public List<DistrictInfo> listByParentId(Long parentId) {
        return this.list(new LambdaQueryWrapper<DistrictInfo>()
                .eq(DistrictInfo::getParentId, parentId)
                .orderByAsc(DistrictInfo::getSortOrder));
    }

    @Override
    public List<DistrictInfo> listByLevel(String level) {
        return this.list(new LambdaQueryWrapper<DistrictInfo>()
                .eq(DistrictInfo::getLevel, level)
                .orderByAsc(DistrictInfo::getSortOrder));
    }
}
