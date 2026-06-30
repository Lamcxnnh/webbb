package com.seig.rental.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 公寓信息
 */
@Data
@TableName(value = "apartment_info", autoResultMap = true)
public class ApartmentInfo {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 公寓名称 */
    private String name;

    /** 公寓介绍 */
    private String intro;

    /** 区县ID */
    private Long districtId;

    /** 详细地址 */
    private String detailAddress;

    /** 纬度 */
    private BigDecimal latitude;

    /** 经度 */
    private BigDecimal longitude;

    /** 封面图片URL */
    private String coverUrl;

    /** 图片列表 (JSON数组) */
    private String images;

    /** 配套ID列表 (JSON数组) */
    private String facilityIds;

    /** 联系电话 */
    private String phone;

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
