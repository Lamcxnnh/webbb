package com.seig.rental.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 房间保存/更新请求
 */
@Data
public class RoomSaveDTO {

    private Long id;
    private Long apartmentId;
    private String name;
    private String intro;
    private BigDecimal price;
    private BigDecimal area;
    private String floor;
    private String roomType;
    private String coverUrl;
    private String images;
    private String attrValueIds;
    private Integer isRelease;
    private Integer sortOrder;

    /** 支付方式ID列表 */
    private List<Long> paymentTypeIds;
    /** 租期ID列表 */
    private List<Long> leaseTermIds;
    /** 标签ID列表 */
    private List<Long> labelIds;
    /** 配套ID列表 */
    private List<Long> facilityIds;
}
