package com.seig.rental.model.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seig.rental.model.entity.DistrictInfo;

import java.util.List;

public interface DistrictInfoService extends IService<DistrictInfo> {

    /** 根据父级ID查询地区列表 */
    List<DistrictInfo> listByParentId(Long parentId);

    /** 根据级别查询地区列表 */
    List<DistrictInfo> listByLevel(String level);
}
