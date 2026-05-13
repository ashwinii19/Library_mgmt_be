package com.libr.mng.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "book_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    private LocalDate requestDate;

    @Column(nullable = false)
    private String requestStatus;

    private String adminRemarks;
    
    private LocalDate processedDate;

    @PrePersist
    protected void onCreate() {
        requestDate = LocalDate.now();
        if (requestStatus == null) requestStatus = "PENDING";
    }
 
}