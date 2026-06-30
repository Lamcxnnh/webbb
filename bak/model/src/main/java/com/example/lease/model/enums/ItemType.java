package com.example.lease.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;


public enum ItemType implements BaseEnum {

    APARTMENT(1, "公寓"),

    ROOM(2, "房间");


    @EnumValue      //MybatisPlus框架提供TypeHandler处理器，可以将数据库类型值  与   [实体类]枚举属性   进行  相互  类型转换
    @JsonValue      // 服务器端响应数据时，将枚举对象值 转换为 JSON属性值
    private Integer code;
    private String name;

    @Override
    public Integer getCode() {
        return this.code;
    }


    @Override
    public String getName() {
        return name;
    }

    ItemType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

}
