package com.libr.mng.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "book_issues")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookIssue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long issueId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;               // the borrower

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    // Librarian who issued the book (optional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issued_by")
    private User issuedBy;

    private LocalDate issueDate;

    private LocalDate dueDate;

    private LocalDate returnDate;

    @Column(nullable = false)
    private String issueStatus;       // "ISSUED", "RETURNED", "OVERDUE", "LOST"

    private Integer renewalCount = 0;

    // Relationships
    @OneToMany(mappedBy = "bookIssue", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RenewalRequest> renewalRequests = new ArrayList<>();

    @OneToOne(mappedBy = "bookIssue", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private BookReturn bookReturn;

    @PrePersist
    protected void onIssue() {
        issueDate = LocalDate.now();
        if (issueStatus == null) issueStatus = "ISSUED";
    }
}
 