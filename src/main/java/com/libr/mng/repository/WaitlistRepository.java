package com.libr.mng.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.libr.mng.entity.Waitlist;

public interface WaitlistRepository extends JpaRepository<Waitlist, Long> {

	Optional<Waitlist> findByUserIdAndBookBookId(Long userId, Long bookId);

	List<Waitlist> findByBookBookIdAndStatus(Long bookId, String status);
}