package com.libr.mng.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(unique = true)
    private String isbnNumber;

    private String category;

    private String publisher;

    private Integer publicationYear;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer totalCopies;

    private Integer availableCopies;
    
    private String imageUrl;

    @Column(nullable = false)
    private String bookStatus;

    private LocalDate addedDate;

    @PrePersist
    protected void onAdd() {
        addedDate = LocalDate.now();
        if (bookStatus == null) bookStatus = "AVAILABLE";
        if (availableCopies == null) availableCopies = totalCopies;
    }

    // Relationships
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BookRequest> bookRequests = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BookIssue> bookIssues = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Wishlist> wishlists = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Waitlist> waitlists = new ArrayList<>();

}