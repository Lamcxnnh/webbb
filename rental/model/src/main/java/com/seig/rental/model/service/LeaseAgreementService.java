package com.seig.rental.model.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seig.rental.model.entity.LeaseAgreement;

public interface LeaseAgreementService extends IService<LeaseAgreement> {

    /** 条件分页查询租约 */
    IPage<LeaseAgreement> pageQuery(IPage<LeaseAgreement> page, String phone, Long apartmentId,
                                     Long roomId, String status);

    /** 更新租约状态 */
    void updateStatus(Long id, String status);

    /** 定时检查并更新到期租约状态 */
    int checkExpiredLeases();
}
