package com.seig.rental.model.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seig.rental.model.entity.ViewAppointment;

public interface ViewAppointmentService extends IService<ViewAppointment> {

    /** 条件分页查询预约 */
    IPage<ViewAppointment> pageQuery(IPage<ViewAppointment> page, String name, String phone,
                                      Long apartmentId, Long roomId, String status);

    /** 更新预约状态 */
    void updateStatus(Long id, String status, String cancelReason);
}
