package com.libr.mng.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RenewalRequestDTO {

    @NotNull(message = "Issue ID is required")
    private Long issueId;

    @NotNull(message = "User ID is required")
    private Long userId;
}