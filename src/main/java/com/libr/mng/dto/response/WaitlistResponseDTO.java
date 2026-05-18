package com.libr.mng.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WaitlistResponseDTO {

    private Long waitlistId;
    private Long userId;
    private String userName;
    private Long bookId;
    private String bookTitle;
    private LocalDateTime joinedAt;
    private Integer position;
    private String status;   // "WAITING", "NOTIFIED", "FULFILLED", "EXPIRED"
}