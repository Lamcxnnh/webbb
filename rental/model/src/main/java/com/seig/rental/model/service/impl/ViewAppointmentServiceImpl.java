package com.seig.rental.model.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seig.rental.model.entity.ViewAppointment;
import com.seig.rental.model.mapper.ViewAppointmentMapper;
import com.seig.rental.model.service.ViewAppointmentService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ViewAppointmentServiceImpl extends ServiceImpl<ViewAppointmentMapper, ViewAppointment>
        implements ViewAppointmentService {

    @Override
    public IPage<ViewAppointment> pageQuery(IPage<ViewAppointment> page, String name, String phone,
                                             Long apartmentId, Long roomId, String status) {
        LambdaQueryWrapper<ViewAppointment> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like(ViewAppointment::getName, name);
        }
        if (StringUtils.hasText(phone)) {
            wrapper.like(ViewAppointment::getPhone, phone);
        }
        if (apartmentId != null) {
            wrapper.eq(ViewAppointment::getApartmentId, apartmentId);
        }
        if (roomId != null) {
            wrapper.eq(ViewAppointment::getRoomId, roomId);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(ViewAppointment::getStatus, status);
        }
        wrapper.orderByDesc(ViewAppointment::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public void updateStatus(Long id, String status, String cancelReason) {
        ViewAppointment appointment = new ViewAppointment();
        appointment.setId(id);
        appointment.setStatus(status);
        if (cancelReason != null) {
            appointment.setCancelReason(cancelReason);
        }
        this.updateById(appointment);
    }
}
