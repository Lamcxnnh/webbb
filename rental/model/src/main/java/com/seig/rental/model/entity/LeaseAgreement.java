package com.seig.rental.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 租约
 */
@Data
@TableName("lease_agreement")
public class LeaseAgreement {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 房间ID */
    private Long roomId;

    /** 公寓ID */
    private Long apartmentId;

    /** 租期ID */
    private Long leaseTermId;

    /** 支付方式ID */
    private Long paymentTypeId;

    /** 租赁开始日期 */
    private LocalDate startDate;

    /** 租赁结束日期 */
    private LocalDate endDate;

    /** 月租金 */
    private BigDecimal rentAmount;

    /** 押金 */
    private BigDecimal depositAmount;

    /** 状态: signed=已签订, active=执行中, expired=已到期, terminated=已解约 */
    private String status;

    /** 签订时间 */
    private LocalDateTime signTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
