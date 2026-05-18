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
public class BookBorrowResponseDTO {

    private Long requestId;
    private Long userId;
    private String userName;
    private Long bookId;
    private String bookTitle;
    private LocalDate requestDate;
    private String requestStatus;  // "PENDING", "APPROVED", "REJECTED"
    private String adminRemarks;
    private LocalDate processedDate;
}