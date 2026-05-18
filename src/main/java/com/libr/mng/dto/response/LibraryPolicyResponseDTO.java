package com.libr.mng.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibraryPolicyResponseDTO {

    private Long policyId;
    private Integer maxBorrowDays;
    private Integer maxBooksAllowed;
    private LocalTime libraryOpenTime;
    private LocalTime libraryCloseTime;
    private BigDecimal lateFinePerDay;
    private Integer suspensionDays;
}