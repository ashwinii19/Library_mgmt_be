package com.libr.mng.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Table(name = "library_policies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibraryPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long policyId;

    private Integer maxBorrowDays;

    private Integer maxBooksAllowed;

    private LocalTime libraryOpenTime;

    private LocalTime libraryCloseTime;

    @Column(precision = 10, scale = 2)
    private BigDecimal lateFinePerDay;

    private Integer suspensionDays;
}