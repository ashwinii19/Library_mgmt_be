package com.libr.mng.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponseDTO {

    private Long notificationId;

    private String title;

    private String message;

    private String type;

    private Boolean isRead;

    private LocalDateTime createdAt;
}