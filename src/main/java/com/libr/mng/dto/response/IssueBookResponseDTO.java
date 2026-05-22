package com.libr.mng.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueBookResponseDTO {

    private Long requestId;

    private Long bookId;

    private String title;

    private String requestStatus;

    private String message;
}