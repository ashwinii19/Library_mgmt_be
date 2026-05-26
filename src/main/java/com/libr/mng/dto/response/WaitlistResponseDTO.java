package com.libr.mng.dto.response;

import lombok.Data;

@Data
public class WaitlistResponseDTO {

    private Long waitlistId;

    private Long bookId;

    private String title;

    private String status;
}