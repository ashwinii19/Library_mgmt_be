package com.libr.mng.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.libr.mng.dto.response.IssuedBookResponseDTO;
import com.libr.mng.service.IssuedBookService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/issued-books")
@RequiredArgsConstructor
public class IssuedBookController {

	private final IssuedBookService issuedBookService;

	@GetMapping
	@PreAuthorize("hasRole('EMPLOYEE')")
	public ResponseEntity<List<IssuedBookResponseDTO>> getMyIssuedBooks() {

		return ResponseEntity.ok(issuedBookService.getMyIssuedBooks());
	}
}