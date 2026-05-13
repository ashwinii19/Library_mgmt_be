package com.libr.mng.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auditLogId;

    @Column(nullable = false)
    private String userType;        // "ADMIN", "USER", "LIBRARIAN"

    @Column(nullable = false)
    private Long userId;            // ID of the user who performed action

    @Column(nullable = false)
    private String actionType;      // "LOGIN", "LOGOUT", "ISSUE_BOOK", "RETURN_BOOK", "ADD_BOOK", "DELETE_BOOK", etc.

    @Column(columnDefinition = "TEXT")
    private String actionDescription; // e.g., "User John issued book 'The Great Gatsby'"

    private LocalDateTime actionDate;

    private String ipAddress;       // optional – store client IP

    @PrePersist
    protected void onCreate() {
        actionDate = LocalDateTime.now();
    }
}
 