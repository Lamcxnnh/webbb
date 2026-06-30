package com.seig.rental.model.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seig.rental.model.entity.RoomInfo;

import java.util.List;

public interface RoomInfoService extends IService<RoomInfo> {

    /**
     * 保存或更新房间信息（含关联表）
     *
     * @param roomInfo       房间基本信息
     * @param paymentTypeIds 支付方式ID列表
     * @param leaseTermIds   租期ID列表
     * @param labelIds       标签ID列表
     * @param facilityIds    配套ID列表
     */
    void saveOrUpdateRoom(RoomInfo roomInfo,
                          List<Long> paymentTypeIds,
                          List<Long> leaseTermIds,
                          List<Long> labelIds,
                          List<Long> facilityIds);

    /** 条件分页查询房间 */
    IPage<RoomInfo> pageQuery(IPage<RoomInfo> page, Long apartmentId, String name, Integer isRelease);

    /** 修改发布状态 */
    void updateReleaseStatus(Long id, Integer isRelease);

    /** 根据公寓ID查询房间列表 */
    List<RoomInfo> listByApartmentId(Long apartmentId);
}
