package com.libr.mng.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.libr.mng.dto.request.ApproveRejectRequestDTO;
import com.libr.mng.dto.response.AdminBookRequestResponseDTO;
import com.libr.mng.dto.response.AdminDashboardResponseDTO;
import com.libr.mng.service.AdminService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

	private final AdminService adminService;

	@GetMapping("/dashboard")
	public ResponseEntity<AdminDashboardResponseDTO> getDashboardStats() {

		return ResponseEntity.ok(adminService.getDashboardStats());
	}

	@GetMapping("/book-requests")
	public ResponseEntity<List<AdminBookRequestResponseDTO>> getPendingRequests() {

		return ResponseEntity.ok(adminService.getPendingRequests());
	}

	@PutMapping("/book-requests/{requestId}/approve")
	public ResponseEntity<String> approveRequest(@PathVariable Long requestId,
			@Valid @RequestBody ApproveRejectRequestDTO dto) {

		return ResponseEntity.ok(adminService.approveRequest(requestId, dto));
	}

	@PutMapping("/book-requests/{requestId}/reject")
	public ResponseEntity<String> rejectRequest(@PathVariable Long requestId,
			@Valid @RequestBody ApproveRejectRequestDTO dto) {

		return ResponseEntity.ok(adminService.rejectRequest(requestId, dto));
	}
}