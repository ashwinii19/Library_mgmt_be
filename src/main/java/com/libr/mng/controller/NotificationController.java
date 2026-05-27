package com.libr.mng.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.libr.mng.dto.response.NotificationCountResponseDTO;
import com.libr.mng.dto.response.NotificationResponseDTO;
import com.libr.mng.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;

	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
	public ResponseEntity<List<NotificationResponseDTO>> getNotifications() {

		return ResponseEntity.ok(notificationService.getNotifications());
	}

	@GetMapping("/unread-count")
	@PreAuthorize("hasRole('EMPLOYEE')")
	public ResponseEntity<NotificationCountResponseDTO> getUnreadCount() {

		return ResponseEntity.ok(notificationService.getUnreadCount());
	}

	@PutMapping("/{notificationId}/read")
	@PreAuthorize("hasRole('EMPLOYEE')")
	public ResponseEntity<String> markAsRead(@PathVariable Long notificationId) {

		return ResponseEntity.ok(notificationService.markAsRead(notificationId));
	}

	@PutMapping("/read-all")
	@PreAuthorize("hasRole('EMPLOYEE')")
	public ResponseEntity<String> markAllAsRead() {

		return ResponseEntity.ok(notificationService.markAllAsRead());
	}

	@DeleteMapping("/{notificationId}")
	@PreAuthorize("hasRole('EMPLOYEE')")
	public ResponseEntity<String> deleteNotification(@PathVariable Long notificationId) {

		return ResponseEntity.ok(notificationService.deleteNotification(notificationId));
	}
}