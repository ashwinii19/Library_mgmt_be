package com.libr.mng.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.libr.mng.entity.BookRequest;

public interface BookRequestRepository extends JpaRepository<BookRequest, Long> {

	Optional<BookRequest> findByUserIdAndBookBookIdAndRequestStatus(Long userId, Long bookId, String requestStatus);

	Page<BookRequest> findByRequestStatus(String requestStatus, Pageable pageable);

	Long countByRequestStatus(String requestStatus);
}