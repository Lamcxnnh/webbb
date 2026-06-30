package com.seig.rental.model.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seig.rental.model.entity.LabelInfo;
import com.seig.rental.model.mapper.LabelInfoMapper;
import com.seig.rental.model.service.LabelInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelInfoServiceImpl extends ServiceImpl<LabelInfoMapper, LabelInfo> implements LabelInfoService {

    @Override
    public List<LabelInfo> listByType(String type) {
        return this.list(new LambdaQueryWrapper<LabelInfo>()
                .eq(LabelInfo::getType, type)
                .orderByAsc(LabelInfo::getSortOrder));
    }
}
