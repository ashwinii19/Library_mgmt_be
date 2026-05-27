package com.libr.mng.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.libr.mng.dto.request.LibraryPolicyRequestDTO;
import com.libr.mng.dto.response.LibraryPolicyResponseDTO;
import com.libr.mng.service.LibraryPolicyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/library-policy")
@RequiredArgsConstructor
public class LibraryPolicyController {

	private final LibraryPolicyService libraryPolicyService;

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<LibraryPolicyResponseDTO> createPolicy(@RequestBody LibraryPolicyRequestDTO dto) {

		return ResponseEntity.ok(libraryPolicyService.createPolicy(dto));
	}

	@PutMapping("/{policyId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<LibraryPolicyResponseDTO> updatePolicy(@PathVariable Long policyId,
			@RequestBody LibraryPolicyRequestDTO dto) {

		return ResponseEntity.ok(libraryPolicyService.updatePolicy(policyId, dto));
	}

	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public ResponseEntity<LibraryPolicyResponseDTO> getPolicy() {

		return ResponseEntity.ok(libraryPolicyService.getPolicy());
	}
}