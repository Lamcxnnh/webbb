package com.seig.rental.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 租期
 */
@Data
@TableName("lease_term")
public class LeaseTerm {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 月数 */
    private Integer monthCount;

    /** 单位 */
    private String unit;

    private Integer sortOrder;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
