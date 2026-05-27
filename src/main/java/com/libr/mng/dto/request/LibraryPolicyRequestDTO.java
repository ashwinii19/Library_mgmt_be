package com.libr.mng.dto.request;

import java.math.BigDecimal;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibraryPolicyRequestDTO {

    private Integer maxBorrowDays;

    private Integer maxBooksAllowed;

    private LocalTime libraryOpenTime;

    private LocalTime libraryCloseTime;

    private BigDecimal lateFinePerDay;

    private Integer suspensionDays;
}