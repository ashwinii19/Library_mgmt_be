package com.libr.mng.dto.response;

import lombok.Data;

@Data
public class WishlistResponseDTO {

    private Long wishlistId;

    private Long bookId;

    private String title;

    private String author;

    private String category;

    private String imageUrl;

    private Integer availableCopies;

    private Integer totalCopies;

    private String bookStatus;

    private boolean issueAllowed;
    
    private boolean notifyAvailable;
}