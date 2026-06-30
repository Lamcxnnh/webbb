package com.seig.rental.model.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seig.rental.model.entity.LabelInfo;

import java.util.List;

public interface LabelInfoService extends IService<LabelInfo> {

    /** 根据类型查询标签列表 */
    List<LabelInfo> listByType(String type);
}
