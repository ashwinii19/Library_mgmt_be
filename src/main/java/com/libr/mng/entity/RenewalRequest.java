package com.libr.mng.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "renewal_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RenewalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long renewalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id", nullable = false)
    private BookIssue bookIssue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDate requestDate;

    @Column(nullable = false)
    private String requestStatus;     // "PENDING", "APPROVED", "REJECTED"

    private LocalDate approvedNewDueDate;

    private String adminRemarks;

    @PrePersist
    protected void onCreate() {
        requestDate = LocalDate.now();
        if (requestStatus == null) requestStatus = "PENDING";
    }

    private LocalDate extendedDueDate;
}