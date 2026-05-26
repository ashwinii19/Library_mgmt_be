package com.libr.mng.serviceImpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.libr.mng.dto.response.WishlistResponseDTO;
import com.libr.mng.entity.AuditLog;
import com.libr.mng.entity.Book;
import com.libr.mng.entity.User;
import com.libr.mng.entity.Waitlist;
import com.libr.mng.entity.Wishlist;
import com.libr.mng.exception.ResourceNotFoundException;
import com.libr.mng.repository.AuditLogRepository;
import com.libr.mng.repository.BookRepository;
import com.libr.mng.repository.WaitlistRepository;
import com.libr.mng.repository.WishlistRepository;
import com.libr.mng.security.CustomUserDetails;
import com.libr.mng.service.WishlistService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

	private final WishlistRepository wishlistRepository;
	private final BookRepository bookRepository;
	private final AuditLogRepository auditLogRepository;
	private final WaitlistRepository waitlistRepository;
	private final ModelMapper modelMapper;

	private User getLoggedInUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

		return userDetails.getUser();
	}

	@Override
	public String addToWishlist(Long bookId) {

		User user = getLoggedInUser();

		Book book = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found"));

		if (wishlistRepository.existsByUserIdAndBookBookId(user.getId(), bookId)) {

			throw new IllegalStateException("Book already in wishlist");
		}

		Wishlist wishlist = Wishlist.builder().user(user).book(book).build();

		wishlistRepository.save(wishlist);

		auditLogRepository.save(AuditLog.builder().userId(user.getId()).userType("EMPLOYEE").actionType("ADD_WISHLIST")
				.actionDescription(user.getName() + " added " + book.getTitle() + " to wishlist").build());

		return "Book added to wishlist";
	}

	@Override
	public String removeFromWishlist(Long bookId) {

		User user = getLoggedInUser();

		Wishlist wishlist = wishlistRepository.findByUserIdAndBookBookId(user.getId(), bookId)
				.orElseThrow(() -> new ResourceNotFoundException("Wishlist entry not found"));

		wishlistRepository.delete(wishlist);

		auditLogRepository
				.save(AuditLog.builder().userId(user.getId()).userType("EMPLOYEE").actionType("REMOVE_WISHLIST")
						.actionDescription(user.getName() + " removed book from wishlist").build());

		return "Book removed from wishlist";
	}

	@Override
	public List<WishlistResponseDTO> getWishlist() {

		User user = getLoggedInUser();

		return wishlistRepository.findByUserId(user.getId()).stream().map(wishlist -> {

			Book book = wishlist.getBook();

			WishlistResponseDTO dto = modelMapper.map(book, WishlistResponseDTO.class);

			dto.setWishlistId(wishlist.getWishlistId());

			dto.setBookId(book.getBookId());

			dto.setIssueAllowed(book.getAvailableCopies() > 0);

			dto.setNotifyAvailable(book.getAvailableCopies() == 0);

			return dto;

		}).toList();
	}

	@Override
	public String notifyWhenAvailable(Long bookId) {

		User user = getLoggedInUser();

		Book book = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found"));

		if (book.getAvailableCopies() > 0) {

			throw new IllegalStateException("Book is already available");
		}

		boolean exists = waitlistRepository.findByUserIdAndBookBookId(user.getId(), bookId).isPresent();

		if (exists) {

			throw new IllegalStateException("Already subscribed");
		}

		Waitlist waitlist = Waitlist.builder().user(user).book(book).status("WAITING").build();

		waitlistRepository.save(waitlist);

		return "You will be notified when the book becomes available";
	}
}