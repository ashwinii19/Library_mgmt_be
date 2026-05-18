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
public class BookReturnResponseDTO {

    private Long returnId;

    // Issue reference
    private Long issueId;
    private Long bookId;
    private String bookTitle;
    private Long userId;
    private String userName;

    // Librarian/Admin who processed return
    private Long processedById;
    private String processedByName;

    private LocalDate returnDate;
    private String returnCondition;   // "GOOD", "DAMAGED", "LOST"
    private String remarks;
}