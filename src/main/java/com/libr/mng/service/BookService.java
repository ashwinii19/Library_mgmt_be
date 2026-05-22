package com.libr.mng.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.libr.mng.dto.request.BookRequestDTO;
import com.libr.mng.dto.request.BookUpdateRequestDTO;
import com.libr.mng.dto.response.BookResponseDTO;

public interface BookService {

	BookResponseDTO addBook(BookRequestDTO dto, MultipartFile image) throws IOException;

	BookResponseDTO updateBook(Long bookId, BookUpdateRequestDTO dto, MultipartFile image) throws IOException;

	void deleteBook(Long bookId);

	BookResponseDTO getBookById(Long bookId);

	List<BookResponseDTO> getAllBooks();

	List<BookResponseDTO> searchBooks(String keyword);

	List<BookResponseDTO> getBooksByCategory(String category);
}