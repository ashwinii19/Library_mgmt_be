package com.libr.mng.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.libr.mng.dto.response.AdminIssuedBookResponseDTO;
import com.libr.mng.service.AdminMonitoringService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/monitoring")
@RequiredArgsConstructor
public class AdminMonitoringController {

	private final AdminMonitoringService adminMonitoringService;

	@GetMapping("/issued-books")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<AdminIssuedBookResponseDTO>> getAllIssuedBooks() {

		return ResponseEntity.ok(adminMonitoringService.getAllIssuedBooks());
	}

	@GetMapping("/overdue-books")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<AdminIssuedBookResponseDTO>> getOverdueBooks() {

		return ResponseEntity.ok(adminMonitoringService.getOverdueBooks());
	}

	@GetMapping("/returned-books")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<AdminIssuedBookResponseDTO>> getReturnedBooks() {

		return ResponseEntity.ok(adminMonitoringService.getReturnedBooks());
	}
}