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
public class BookResponseDTO {

    private Long bookId;
    private String title;
    private String author;
    private String isbnNumber;
    private String category;
    private String publisher;
    private Integer publicationYear;
    private String description;
    private Integer totalCopies;
    private Integer availableCopies;
    private String bookStatus;
    private LocalDate addedDate;
}