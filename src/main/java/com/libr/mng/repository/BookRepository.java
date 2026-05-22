package com.libr.mng.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.libr.mng.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbnNumber(String isbnNumber);

    List<Book> findByCategoryIgnoreCase(String category);

    List<Book> findByTitleContainingIgnoreCase(String keyword);
}