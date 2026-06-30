package com.example.lease.web.admin.custom.converter;

import com.example.lease.model.enums.BaseEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

@Component
public class StringToBaseEnumConverterFactory implements ConverterFactory<String, BaseEnum> {

    @Override
    public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {

        return (source)->{
            // T 泛型表示的就是继承了BaseEnum的具体枚举类型
            //参考：ItemType[] itemTypes = ItemType.values();  //APARTMENT(1, "公寓"),ROOM(2, "房间")
            T[] enumConstants = targetType.getEnumConstants();
            for (T enumConstant : enumConstants) {
                if(enumConstant.getCode().equals(Integer.valueOf(source))){
                    return enumConstant;
                }
            }
            throw new RuntimeException("参数转换为枚举类型失败");
        };
    }
}
