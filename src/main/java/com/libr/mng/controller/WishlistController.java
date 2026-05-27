package com.libr.mng.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.libr.mng.dto.response.WishlistResponseDTO;
import com.libr.mng.service.WishlistService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

	private final WishlistService wishlistService;

	@PostMapping("/{bookId}")
	@PreAuthorize("hasRole('EMPLOYEE')")
	public ResponseEntity<String> addToWishlist(@PathVariable Long bookId) {

		return ResponseEntity.ok(wishlistService.addToWishlist(bookId));
	}

	@DeleteMapping("/{bookId}")
	@PreAuthorize("hasRole('EMPLOYEE')")
	public ResponseEntity<String> removeWishlist(@PathVariable Long bookId) {

		return ResponseEntity.ok(wishlistService.removeFromWishlist(bookId));
	}

	@GetMapping
	@PreAuthorize("hasRole('EMPLOYEE')")
	public ResponseEntity<?> getWishlist() {

		return ResponseEntity.ok(wishlistService.getWishlist());
	}

	@PostMapping("/notify/{bookId}")
	@PreAuthorize("hasRole('EMPLOYEE')")
	public ResponseEntity<String> notifyWhenAvailable(@PathVariable Long bookId) {

		return ResponseEntity.ok(wishlistService.notifyWhenAvailable(bookId));
	}

}