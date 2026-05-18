package com.libr.mng.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
public class LibraryPolicyRequestDTO {

    @NotNull(message = "Max borrow days is required")
    @Min(value = 1, message = "Max borrow days must be at least 1")
    private Integer maxBorrowDays;

    @NotNull(message = "Max books allowed is required")
    @Min(value = 1, message = "Max books allowed must be at least 1")
    private Integer maxBooksAllowed;

    private LocalTime libraryOpenTime;

    private LocalTime libraryCloseTime;

    @DecimalMin(value = "0.0", inclusive = true, message = "Late fine must be non-negative")
    private BigDecimal lateFinePerDay;

    @Min(value = 0, message = "Suspension days must be non-negative")
    private Integer suspensionDays;
}