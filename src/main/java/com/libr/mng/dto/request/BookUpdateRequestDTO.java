package com.libr.mng.dto.request;

import lombok.Data;

@Data
public class BookUpdateRequestDTO {

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
}