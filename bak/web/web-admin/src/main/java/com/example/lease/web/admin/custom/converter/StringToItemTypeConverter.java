package com.example.lease.web.admin.custom.converter;

import com.example.lease.model.enums.ItemType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * 类型转换器：
 *      将请求参数字符串值转换为对应枚举值。
 */
@Component
public class StringToItemTypeConverter implements Converter<String, ItemType> {

    @Override
    public ItemType convert(String source) {
        ItemType[] itemTypes = ItemType.values();
        for (ItemType itemType : itemTypes) {
            if(itemType.getCode().equals(Integer.valueOf(source))){
                return itemType;
            }
        }
        throw new RuntimeException("参数转换为枚举类型失败");
    }
}
