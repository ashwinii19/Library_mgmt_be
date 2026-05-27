package com.libr.mng.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.libr.mng.dto.request.*;
import com.libr.mng.dto.response.RenewalResponseDTO;
import com.libr.mng.service.RenewalService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/renewals")
@RequiredArgsConstructor
public class RenewalController {

	private final RenewalService renewalService;

	@PostMapping("/request/{issueId}")
	@PreAuthorize("hasRole('EMPLOYEE')")
	public ResponseEntity<String> requestRenewal(@PathVariable Long issueId, @RequestBody RenewalRequestDTO dto) {

		return ResponseEntity.ok(renewalService.requestRenewal(issueId, dto));
	}

	@GetMapping("/my")
	@PreAuthorize("hasRole('EMPLOYEE')")
	public ResponseEntity<List<RenewalResponseDTO>> myRenewals() {

		return ResponseEntity.ok(renewalService.getMyRenewalRequests());
	}

	@GetMapping("/pending")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<RenewalResponseDTO>> pendingRenewals() {

		return ResponseEntity.ok(renewalService.getPendingRenewalRequests());
	}

	@PostMapping("/{renewalId}/approve")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> approveRenewal(@PathVariable Long renewalId, @RequestBody RenewalApprovalDTO dto) {

		return ResponseEntity.ok(renewalService.approveRenewal(renewalId, dto));
	}

	@PostMapping("/{renewalId}/reject")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> rejectRenewal(@PathVariable Long renewalId, @RequestBody RenewalApprovalDTO dto) {

		return ResponseEntity.ok(renewalService.rejectRenewal(renewalId, dto));
	}
}