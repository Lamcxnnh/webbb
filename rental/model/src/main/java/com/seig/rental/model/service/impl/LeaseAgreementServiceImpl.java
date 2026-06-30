package com.seig.rental.model.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seig.rental.model.entity.LeaseAgreement;
import com.seig.rental.model.mapper.LeaseAgreementMapper;
import com.seig.rental.model.service.LeaseAgreementService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

@Service
public class LeaseAgreementServiceImpl extends ServiceImpl<LeaseAgreementMapper, LeaseAgreement>
        implements LeaseAgreementService {

    @Override
    public IPage<LeaseAgreement> pageQuery(IPage<LeaseAgreement> page, String phone, Long apartmentId,
                                            Long roomId, String status) {
        LambdaQueryWrapper<LeaseAgreement> wrapper = new LambdaQueryWrapper<>();
        if (apartmentId != null) {
            wrapper.eq(LeaseAgreement::getApartmentId, apartmentId);
        }
        if (roomId != null) {
            wrapper.eq(LeaseAgreement::getRoomId, roomId);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(LeaseAgreement::getStatus, status);
        }
        if (StringUtils.hasText(phone)) {
            // phone 在 user_info 表中，这里简化处理：通过 user_id 子查询
            // 暂时留空，后续关联查询
        }
        wrapper.orderByDesc(LeaseAgreement::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public void updateStatus(Long id, String status) {
        LeaseAgreement agreement = new LeaseAgreement();
        agreement.setId(id);
        agreement.setStatus(status);
        this.updateById(agreement);
    }

    @Override
    public int checkExpiredLeases() {
        // 将执行中且已到结束日期的租约标记为"已到期"
        return (int) this.baseMapper.update(null,
                new LambdaUpdateWrapper<LeaseAgreement>()
                        .eq(LeaseAgreement::getStatus, "active")
                        .le(LeaseAgreement::getEndDate, LocalDate.now())
                        .set(LeaseAgreement::getStatus, "expired"));
    }
}
