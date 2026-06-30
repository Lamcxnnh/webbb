package com.seig.rental.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 看房预约
 */
@Data
@TableName("view_appointment")
public class ViewAppointment {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 房间ID */
    private Long roomId;

    /** 公寓ID */
    private Long apartmentId;

    /** 预约看房时间 */
    private LocalDateTime appointmentTime;

    /** 联系人姓名 */
    private String name;

    /** 联系人电话 */
    private String phone;

    /** 备注 */
    private String remark;

    /** 状态: pending=待确认, confirmed=已确认, cancelled=已取消, completed=已完成 */
    private String status;

    /** 取消原因 */
    private String cancelReason;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
