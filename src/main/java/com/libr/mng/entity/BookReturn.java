package com.libr.mng.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "book_returns")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookReturn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long returnId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id", nullable = false)
    private BookIssue bookIssue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processed_by", nullable = false)
    private User processedBy;        // librarian/admin

    private LocalDate returnDate;

    @Column(nullable = false)
    private String returnCondition;   // "GOOD", "DAMAGED", "LOST"

    private String remarks;

    @PrePersist
    protected void onReturn() {
        returnDate = LocalDate.now();
        if (returnCondition == null) returnCondition = "GOOD";
    }
}
 