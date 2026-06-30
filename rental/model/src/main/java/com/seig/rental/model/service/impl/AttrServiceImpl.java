package com.seig.rental.model.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seig.rental.model.entity.AttrKey;
import com.seig.rental.model.entity.AttrValue;
import com.seig.rental.model.mapper.AttrKeyMapper;
import com.seig.rental.model.mapper.AttrValueMapper;
import com.seig.rental.model.service.AttrService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AttrServiceImpl implements AttrService {

    @Resource
    private AttrKeyMapper attrKeyMapper;

    @Resource
    private AttrValueMapper attrValueMapper;

    // ===== 属性名称 =====

    @Override
    public List<AttrKey> listKeys() {
        return attrKeyMapper.selectList(new LambdaQueryWrapper<AttrKey>()
                .orderByAsc(AttrKey::getSortOrder));
    }

    @Override
    public void saveOrUpdateKey(AttrKey attrKey) {
        if (attrKey.getId() == null) {
            attrKeyMapper.insert(attrKey);
        } else {
            attrKeyMapper.updateById(attrKey);
        }
    }

    @Override
    @Transactional
    public void deleteKey(Long id) {
        // 删除属性名称时，同时删除其下所有属性值
        attrKeyMapper.deleteById(id);
        attrValueMapper.delete(new LambdaQueryWrapper<AttrValue>()
                .eq(AttrValue::getKeyId, id));
    }

    // ===== 属性值 =====

    @Override
    public List<AttrValue> listValuesByKeyId(Long keyId) {
        return attrValueMapper.selectList(new LambdaQueryWrapper<AttrValue>()
                .eq(AttrValue::getKeyId, keyId)
                .orderByAsc(AttrValue::getSortOrder));
    }

    @Override
    public void saveOrUpdateValue(AttrValue attrValue) {
        if (attrValue.getId() == null) {
            attrValueMapper.insert(attrValue);
        } else {
            attrValueMapper.updateById(attrValue);
        }
    }

    @Override
    public void deleteValue(Long id) {
        attrValueMapper.deleteById(id);
    }

    // ===== 组合查询 =====

    @Override
    public List<AttrKey> listAllWithValues() {
        List<AttrKey> keys = listKeys();
        for (AttrKey key : keys) {
            key.setDeleted(null); // clear logic delete field for response
        }
        return keys; // 前端可以逐项获取属性值
    }
}
