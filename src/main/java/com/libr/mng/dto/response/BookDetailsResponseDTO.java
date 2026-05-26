package com.libr.mng.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDetailsResponseDTO {

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

    private String imageUrl;

    private String bookStatus;

    private boolean issueAllowed;

    private boolean wishlistAllowed;

    private boolean alreadyWishlisted;

    private boolean alreadyRequested;

    private boolean notifyAvailable;
}