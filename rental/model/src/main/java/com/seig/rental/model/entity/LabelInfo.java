package com.seig.rental.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 标签信息
 */
@Data
@TableName("label_info")
public class LabelInfo {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 标签类型: room / apartment */
    private String type;

    /** 标签名称 */
    private String name;

    private Integer sortOrder;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
