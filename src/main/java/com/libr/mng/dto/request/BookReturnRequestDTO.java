package com.libr.mng.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookReturnRequestDTO {

    @NotNull(message = "Issue ID is required")
    private Long issueId;

    @NotNull(message = "Processed By (librarian/admin) ID is required")
    private Long processedById;

    @NotBlank(message = "Return condition is required")
    private String returnCondition;   // "GOOD", "DAMAGED", "LOST"

    private String remarks;
}