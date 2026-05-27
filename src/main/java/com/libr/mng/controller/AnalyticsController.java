package com.libr.mng.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.libr.mng.dto.response.AnalyticsResponseDTO;
import com.libr.mng.service.AnalyticsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

	private final AnalyticsService analyticsService;

	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<AnalyticsResponseDTO> getAnalytics() {

		return ResponseEntity.ok(analyticsService.getAnalytics());
	}
}