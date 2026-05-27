package com.libr.mng.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.libr.mng.dto.request.IssueBookRequestDTO;
import com.libr.mng.dto.response.BookDetailsResponseDTO;
import com.libr.mng.dto.response.InventoryResponseDTO;
import com.libr.mng.dto.response.IssueBookResponseDTO;
import com.libr.mng.service.InventoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

	private final InventoryService inventoryService;

	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public ResponseEntity<List<InventoryResponseDTO>> getInventory() {

		return ResponseEntity.ok(inventoryService.getInventory());
	}

	@GetMapping("/search")
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public ResponseEntity<List<InventoryResponseDTO>> searchBooks(@RequestParam String keyword) {

		return ResponseEntity.ok(inventoryService.searchBooks(keyword));
	}

	@GetMapping("/category/{category}")
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public ResponseEntity<List<InventoryResponseDTO>> filterByCategory(@PathVariable String category) {

		return ResponseEntity.ok(inventoryService.filterByCategory(category));
	}

	@GetMapping("/{bookId}")
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public ResponseEntity<BookDetailsResponseDTO> getBookDetails(@PathVariable Long bookId) {

		return ResponseEntity.ok(inventoryService.getBookDetails(bookId));
	}

	@PostMapping("/issue")
	@PreAuthorize("hasRole('EMPLOYEE')")
	public ResponseEntity<IssueBookResponseDTO> issueBook(@Valid @RequestBody IssueBookRequestDTO dto) {

		return ResponseEntity.ok(inventoryService.issueBook(dto));
	}
}