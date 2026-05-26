package com.libr.mng.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssuedBookResponseDTO {

    private Long issueId;

    private Long bookId;

    private String title;

    private String author;

    private String imageUrl;

    private LocalDate issueDate;

    private LocalDate dueDate;

    private String issueStatus;

    private Integer renewalCount;

    private Long daysRemaining;

    private boolean overdue;

    private Long overdueDays;

    private boolean renewalAllowed;

    private boolean returnAllowed;

    private boolean showCountdown;

    private Long remainingHours;

    private Long remainingMinutes;

    private Long remainingSeconds;

    private String countdownNote;
}