package com.seig.rental.model.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seig.rental.model.entity.FacilityInfo;
import com.seig.rental.model.mapper.FacilityInfoMapper;
import com.seig.rental.model.service.FacilityInfoService;
import org.springframework.stereotype.Service;

@Service
public class FacilityInfoServiceImpl extends ServiceImpl<FacilityInfoMapper, FacilityInfo> implements FacilityInfoService {
}
