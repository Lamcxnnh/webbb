package com.seig.rental.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 租约保存/更新请求
 */
@Data
public class LeaseAgreementSaveDTO {

    private Long id;
    private Long userId;
    private Long roomId;
    private Long apartmentId;
    private Long leaseTermId;
    private Long paymentTypeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal rentAmount;
    private BigDecimal depositAmount;
    private String status;
}
