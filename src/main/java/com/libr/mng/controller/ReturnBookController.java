package com.libr.mng.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.libr.mng.dto.request.ReturnApprovalDTO;
import com.libr.mng.dto.request.ReturnBookRequestDTO;
import com.libr.mng.dto.response.ReturnBookResponseDTO;
import com.libr.mng.service.ReturnBookService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/returns")
@RequiredArgsConstructor
public class ReturnBookController {

	private final ReturnBookService returnBookService;

	@PostMapping("/request/{issueId}")
	@PreAuthorize("hasRole('EMPLOYEE')")
	public ResponseEntity<String> requestReturn(@PathVariable Long issueId, @RequestBody ReturnBookRequestDTO dto) {

		return ResponseEntity.ok(returnBookService.requestReturn(issueId, dto));
	}

	@GetMapping("/pending")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<ReturnBookResponseDTO>> getPendingReturns() {

		return ResponseEntity.ok(returnBookService.getPendingReturns());
	}

	@PostMapping("/approve/{issueId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> approveReturn(@PathVariable Long issueId, @RequestBody ReturnApprovalDTO dto) {

		return ResponseEntity.ok(returnBookService.approveReturn(issueId, dto));
	}
}