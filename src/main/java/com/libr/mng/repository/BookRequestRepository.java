package com.libr.mng.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.libr.mng.entity.BookRequest;

public interface BookRequestRepository extends JpaRepository<BookRequest, Long> {

	Optional<BookRequest> findByUserIdAndBookBookIdAndRequestStatus(Long userId, Long bookId, String requestStatus);
	
	List<BookRequest> findByRequestStatus(String requestStatus);
}