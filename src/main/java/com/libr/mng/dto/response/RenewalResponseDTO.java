package com.libr.mng.dto.response;

import java.time.LocalDate;

import lombok.Data;

@Data
public class RenewalResponseDTO {

    private Long renewalId;

    private Long issueId;

    private String bookTitle;

    private String employeeName;

    private LocalDate requestDate;

    private String requestStatus;

    private LocalDate extendedDueDate;

    private String adminRemarks;
}