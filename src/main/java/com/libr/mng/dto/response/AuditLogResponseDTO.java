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
public class AuditLogResponseDTO {

    private Long auditLogId;
    private String userType;           // "ADMIN", "USER", "LIBRARIAN"
    private Long userId;
    private String actionType;         // "LOGIN", "LOGOUT", "ISSUE_BOOK", etc.
    private String actionDescription;
    private LocalDateTime actionDate;
    private String ipAddress;
}