package com.libr.mng.entity;
 
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;   // "ADMIN" or "USER"

    @Column(unique = true, nullable = false)
    private Long employeeId;
    
    private LocalDateTime createdAt;
    
    private String resetOtp;
    
    private LocalDateTime otpExpiry;
    
    private String profileImage;  
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
 
    // Relationships
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BookRequest> bookRequests = new ArrayList<>();
 
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BookIssue> bookIssues = new ArrayList<>();
 
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Waitlist> waitlists = new ArrayList<>();
 
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Wishlist> wishlists = new ArrayList<>();
 
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Notification> notifications = new ArrayList<>();
 
    // For librarian/admin who processes returns
    @OneToMany(mappedBy = "processedBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BookReturn> processedReturns = new ArrayList<>();

}
 