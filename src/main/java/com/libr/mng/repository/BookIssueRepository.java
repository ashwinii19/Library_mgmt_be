package com.libr.mng.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.libr.mng.entity.BookIssue;

public interface BookIssueRepository extends JpaRepository<BookIssue, Long> {

	Page<BookIssue> findByUserIdOrderByIssueDateDesc(Long userId, Pageable pageable);

	Page<BookIssue> findByIssueStatus(String issueStatus, Pageable pageable);

	List<BookIssue> findByDueDateAndIssueStatus(LocalDate dueDate, String issueStatus);

	List<BookIssue> findByDueDateBeforeAndIssueStatus(LocalDate date, String issueStatus);
}