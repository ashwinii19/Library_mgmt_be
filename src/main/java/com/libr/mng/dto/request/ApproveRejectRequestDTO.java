package com.libr.mng.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ApproveRejectRequestDTO {

    @NotBlank(message = "Remarks required")
    private String adminRemarks;
}