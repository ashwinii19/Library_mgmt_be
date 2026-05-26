package com.libr.mng.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDashboardResponseDTO {

    private Long totalBooks;

    private Long totalCopies;

    private Long availableCopies;

    private Long pendingRequests;
}