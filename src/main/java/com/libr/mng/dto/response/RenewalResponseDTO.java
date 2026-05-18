package com.libr.mng.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RenewalResponseDTO {

    private Long renewalId;

    private Long issueId;
    private Long bookId;
    private String bookTitle;
    private Long userId;
    private String userName;

    private LocalDate requestDate;
    private String requestStatus;          // "PENDING", "APPROVED", "REJECTED"
    private LocalDate approvedNewDueDate;
    private LocalDate extendedDueDate;
    private String adminRemarks;
}