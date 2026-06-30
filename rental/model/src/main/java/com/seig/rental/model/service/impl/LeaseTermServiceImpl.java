package com.seig.rental.model.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seig.rental.model.entity.LeaseTerm;
import com.seig.rental.model.mapper.LeaseTermMapper;
import com.seig.rental.model.service.LeaseTermService;
import org.springframework.stereotype.Service;

@Service
public class LeaseTermServiceImpl extends ServiceImpl<LeaseTermMapper, LeaseTerm> implements LeaseTermService {
}
