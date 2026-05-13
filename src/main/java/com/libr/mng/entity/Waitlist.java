package com.libr.mng.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "waitlists")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Waitlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long waitlistId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    private LocalDateTime joinedAt;

    private Integer position;    // optional: can be computed

    @Column(nullable = false)
    private String status;       // "WAITING", "NOTIFIED", "FULFILLED", "EXPIRED"

    @PrePersist
    protected void onJoin() {
        joinedAt = LocalDateTime.now();
        if (status == null) status = "WAITING";
    }
}