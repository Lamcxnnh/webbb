package com.seig.rental.model.vo;

import com.seig.rental.model.entity.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 房间详细信息（含关联数据）
 */
@Data
public class RoomDetailVO {

    // 房间基本信息
    private Long id;
    private Long apartmentId;
    private String name;
    private String intro;
    private BigDecimal price;
    private BigDecimal area;
    private String floor;
    private String roomType;
    private String coverUrl;
    private List<String> images;
    private List<Long> attrValueIds;
    private Integer isRelease;
    private Integer sortOrder;

    // 关联数据
    private List<PaymentType> paymentTypes;   // 可选支付方式
    private List<LeaseTerm> leaseTerms;       // 可选租期
    private List<LabelInfo> labels;           // 标签
    private List<FacilityInfo> facilities;    // 配套

    // 所属公寓简要信息
    private String apartmentName;
    private String apartmentCoverUrl;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
