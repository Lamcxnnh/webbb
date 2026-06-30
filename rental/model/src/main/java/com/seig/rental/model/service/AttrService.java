package com.seig.rental.model.service;

import com.seig.rental.model.entity.AttrKey;
import com.seig.rental.model.entity.AttrValue;

import java.util.List;

/**
 * 属性管理服务（管理 attr_key + attr_value）
 */
public interface AttrService {

    // ===== 属性名称 =====
    List<AttrKey> listKeys();
    void saveOrUpdateKey(AttrKey attrKey);
    void deleteKey(Long id);

    // ===== 属性值 =====
    List<AttrValue> listValuesByKeyId(Long keyId);
    void saveOrUpdateValue(AttrValue attrValue);
    void deleteValue(Long id);

    // ===== 组合查询 =====
    /** 查询全部属性名称和对应的属性值列表 */
    List<AttrKey> listAllWithValues();
}
