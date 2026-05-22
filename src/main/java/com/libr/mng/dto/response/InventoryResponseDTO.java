package com.libr.mng.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryResponseDTO {

    private Long bookId;

    private String title;

    private String author;

    private String category;

    private String imageUrl;

    private Integer availableCopies;

    private Integer totalCopies;

    private String bookStatus;
}