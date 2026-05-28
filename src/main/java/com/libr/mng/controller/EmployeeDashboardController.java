package com.libr.mng.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.libr.mng.dto.response.EmployeeDashboardDataResponseDTO;
import com.libr.mng.service.EmployeeDashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employee/dashboard")
@RequiredArgsConstructor
public class EmployeeDashboardController {

	private final EmployeeDashboardService employeeDashboardService;

	@GetMapping
	@PreAuthorize("hasRole('EMPLOYEE')")
	public ResponseEntity<EmployeeDashboardDataResponseDTO> getDashboard() {

		return ResponseEntity.ok(employeeDashboardService.getDashboard());
	}
}