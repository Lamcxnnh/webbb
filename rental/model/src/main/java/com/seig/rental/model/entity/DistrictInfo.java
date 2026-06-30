package com.seig.rental.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 地区信息 (省/市/区县)
 */
@Data
@TableName("district_info")
public class DistrictInfo {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 父级ID (0=省份) */
    private Long parentId;

    /** 地区名称 */
    private String name;

    /** 级别: province / city / district */
    private String level;

    /** 排序 */
    private Integer sortOrder;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
