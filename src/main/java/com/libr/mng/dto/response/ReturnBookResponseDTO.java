package com.libr.mng.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReturnBookResponseDTO {

    private Long returnId;

    private Long issueId;

    private String bookTitle;

    private String employeeName;

    private LocalDate issueDate;

    private LocalDate dueDate;

    private LocalDate returnDate;

    private String issueStatus;

    private String returnCondition;

    private String remarks;
}