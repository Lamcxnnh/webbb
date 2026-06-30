package com.seig.rental.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 房间信息
 */
@Data
@TableName(value = "room_info", autoResultMap = true)
public class RoomInfo {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 所属公寓ID */
    private Long apartmentId;

    /** 房间名称 */
    private String name;

    /** 房间介绍 */
    private String intro;

    /** 租金 (元/月) */
    private BigDecimal price;

    /** 面积 (㎡) */
    private BigDecimal area;

    /** 楼层 */
    private String floor;

    /** 户型 (如: 1室1厅) */
    private String roomType;

    /** 封面图片URL */
    private String coverUrl;

    /** 图片列表 (JSON数组) */
    private String images;

    /** 属性值ID列表 (JSON数组) */
    private String attrValueIds;

    /** 发布状态: 0=未发布, 1=已发布 */
    private Integer isRelease;

    private Integer sortOrder;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
