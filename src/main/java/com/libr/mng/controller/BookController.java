package com.libr.mng.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.libr.mng.dto.request.BookRequestDTO;
import com.libr.mng.dto.request.BookUpdateRequestDTO;
import com.libr.mng.dto.response.BookResponseDTO;
import com.libr.mng.service.BookService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

	private final BookService bookService;

	// ==========================================
	// ADD BOOK (ADMIN ONLY)
	// ==========================================

	@PostMapping(consumes = "multipart/form-data")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BookResponseDTO> addBook(@Valid @ModelAttribute BookRequestDTO dto,
			@RequestParam("image") MultipartFile image) throws IOException {

		BookResponseDTO response = bookService.addBook(dto, image);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// ==========================================
	// UPDATE BOOK (ADMIN ONLY)
	// ==========================================

	@PutMapping(value = "/{bookId}", consumes = "multipart/form-data")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BookResponseDTO> updateBook(@PathVariable Long bookId,

			@ModelAttribute BookUpdateRequestDTO dto,

			@RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

		BookResponseDTO response = bookService.updateBook(bookId, dto, image);

		return ResponseEntity.ok(response);
	}

	// ==========================================
	// DELETE BOOK (ADMIN ONLY)
	// ==========================================

	@DeleteMapping("/{bookId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteBook(@PathVariable Long bookId) {

		bookService.deleteBook(bookId);

		return ResponseEntity.ok("Book deleted successfully");
	}

	// ==========================================
	// GET BOOK BY ID
	// ADMIN + EMPLOYEE
	// ==========================================

	@GetMapping("/{bookId}")
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Long bookId) {

		BookResponseDTO response = bookService.getBookById(bookId);

		return ResponseEntity.ok(response);
	}

	// ==========================================
	// GET ALL BOOKS
	// ADMIN + EMPLOYEE
	// ==========================================

	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public ResponseEntity<List<BookResponseDTO>> getAllBooks() {

		List<BookResponseDTO> response = bookService.getAllBooks();

		return ResponseEntity.ok(response);
	}

	// ==========================================
	// SEARCH BOOKS
	// ADMIN + EMPLOYEE
	// ==========================================

	@GetMapping("/search")
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public ResponseEntity<List<BookResponseDTO>> searchBooks(@RequestParam String keyword) {

		List<BookResponseDTO> response = bookService.searchBooks(keyword);

		return ResponseEntity.ok(response);
	}

	// ==========================================
	// FILTER BY CATEGORY
	// ADMIN + EMPLOYEE
	// ==========================================

	@GetMapping("/category/{category}")
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public ResponseEntity<List<BookResponseDTO>> getBooksByCategory(@PathVariable String category) {

		List<BookResponseDTO> response = bookService.getBooksByCategory(category);

		return ResponseEntity.ok(response);
	}
}