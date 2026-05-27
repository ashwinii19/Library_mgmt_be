package com.libr.mng.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.libr.mng.entity.BookReturn;

public interface BookReturnRepository extends JpaRepository<BookReturn, Long> {

	Optional<BookReturn> findByBookIssueIssueId(Long issueId);
}