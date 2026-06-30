package com.seig.rental.model.vo;

import com.seig.rental.model.entity.FacilityInfo;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 公寓详细信息（含配套列表）
 */
@Data
public class ApartmentDetailVO {

    private Long id;
    private String name;
    private String intro;
    private Long districtId;
    private String detailAddress;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String coverUrl;
    private List<String> images;
    private List<FacilityInfo> facilities;  // 配套列表
    private String phone;
    private Integer isRelease;
    private Integer sortOrder;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
