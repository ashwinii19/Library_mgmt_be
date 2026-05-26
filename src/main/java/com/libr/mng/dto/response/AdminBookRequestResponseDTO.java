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
public class AdminBookRequestResponseDTO {

    private Long requestId;

    private Long employeeId;

    private String employeeName;

    private Long bookId;

    private String bookTitle;

    private LocalDate requestDate;

    private String requestStatus;
}