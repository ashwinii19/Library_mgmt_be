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
public class BookIssueResponseDTO {

    private Long issueId;

    // User details (borrower)
    private Long userId;
    private String userName;

    // Book details
    private Long bookId;
    private String bookTitle;
    private String bookAuthor;

    // Librarian who issued (optional)
    private Long issuedById;
    private String issuedByName;

    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    private String issueStatus;    // "ISSUED", "RETURNED", "OVERDUE", "LOST"
    private Integer renewalCount;
}