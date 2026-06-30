package com.seig.rental.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 配套信息
 */
@Data
@TableName("facility_info")
public class FacilityInfo {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 配套名称 */
    private String name;

    /** 图标URL */
    private String icon;

    private Integer sortOrder;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
