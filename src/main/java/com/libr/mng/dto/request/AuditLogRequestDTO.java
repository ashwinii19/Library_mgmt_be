package com.libr.mng.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogRequestDTO {

    private String userType;           // "ADMIN", "USER", "LIBRARIAN"
    
    private Long userId;               // ID of the user who performed the action
    
    private String actionType;         // "LOGIN", "LOGOUT", "ISSUE_BOOK", "RETURN_BOOK", etc.
    
    private String actionDescription;  // e.g., "User John issued book 'The Great Gatsby'"
    
    private String ipAddress;          // optional – store client IP
    
}