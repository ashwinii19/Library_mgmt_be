package com.libr.mng.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.libr.mng.entity.BookIssue;

public interface BookIssueRepository extends JpaRepository<BookIssue, Long> {

}