package com.seig.rental.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 属性值
 */
@Data
@TableName("attr_value")
public class AttrValue {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 属性名称ID */
    private Long keyId;

    /** 属性值名称 */
    private String name;

    private Integer sortOrder;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
