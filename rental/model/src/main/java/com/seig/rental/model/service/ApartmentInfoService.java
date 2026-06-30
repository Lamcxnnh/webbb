package com.seig.rental.model.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seig.rental.model.entity.ApartmentInfo;

public interface ApartmentInfoService extends IService<ApartmentInfo> {

    /** 条件分页查询公寓 */
    IPage<ApartmentInfo> pageQuery(IPage<ApartmentInfo> page, String name, Long districtId, Integer isRelease);

    /** 修改发布状态 */
    void updateReleaseStatus(Long id, Integer isRelease);
}
