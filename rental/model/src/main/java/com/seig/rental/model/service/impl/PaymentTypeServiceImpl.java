package com.seig.rental.model.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seig.rental.model.entity.PaymentType;
import com.seig.rental.model.mapper.PaymentTypeMapper;
import com.seig.rental.model.service.PaymentTypeService;
import org.springframework.stereotype.Service;

@Service
public class PaymentTypeServiceImpl extends ServiceImpl<PaymentTypeMapper, PaymentType> implements PaymentTypeService {
}
