package com.libr.mng.serviceImpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.libr.mng.dto.request.IssueBookRequestDTO;
import com.libr.mng.dto.response.BookDetailsResponseDTO;
import com.libr.mng.dto.response.InventoryResponseDTO;
import com.libr.mng.dto.response.IssueBookResponseDTO;
import com.libr.mng.entity.AuditLog;
import com.libr.mng.entity.Book;
import com.libr.mng.entity.BookRequest;
import com.libr.mng.entity.Notification;
import com.libr.mng.entity.User;
import com.libr.mng.exception.ResourceNotFoundException;
import com.libr.mng.repository.AuditLogRepository;
import com.libr.mng.repository.BookRepository;
import com.libr.mng.repository.BookRequestRepository;
import com.libr.mng.repository.NotificationRepository;
import com.libr.mng.repository.UserRepository;
import com.libr.mng.repository.WishlistRepository;
import com.libr.mng.security.CustomUserDetails;
import com.libr.mng.service.InventoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

	private final BookRepository bookRepository;
	private final ModelMapper modelMapper;
	private final BookRequestRepository bookRequestRepository;
	private final UserRepository userRepository;
	private final AuditLogRepository auditLogRepository;
	private final NotificationRepository notificationRepository;
	private final WishlistRepository wishlistRepository;

	@Override
	public List<InventoryResponseDTO> getInventory() {

		return bookRepository.findAll().stream().map(book -> modelMapper.map(book, InventoryResponseDTO.class))
				.toList();
	}

	@Override
	public List<InventoryResponseDTO> searchBooks(String keyword) {

		return bookRepository.findByTitleContainingIgnoreCase(keyword).stream()
				.map(book -> modelMapper.map(book, InventoryResponseDTO.class)).toList();
	}

	@Override
	public List<InventoryResponseDTO> filterByCategory(String category) {

		return bookRepository.findByCategoryIgnoreCase(category).stream()
				.map(book -> modelMapper.map(book, InventoryResponseDTO.class)).toList();
	}

	@Override
	public BookDetailsResponseDTO getBookDetails(Long bookId) {

		User user = getLoggedInUser();

		Book book = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found"));

		BookDetailsResponseDTO dto = modelMapper.map(book, BookDetailsResponseDTO.class);

		dto.setIssueAllowed(book.getAvailableCopies() > 0);

		dto.setWishlistAllowed(true);

		dto.setAlreadyWishlisted(wishlistRepository.existsByUserIdAndBookBookId(user.getId(), bookId));

		dto.setAlreadyRequested(bookRequestRepository
				.findByUserIdAndBookBookIdAndRequestStatus(user.getId(), bookId, "PENDING").isPresent());

		dto.setNotifyAvailable(book.getAvailableCopies() == 0);

		return dto;
	}

	@Override
	@Transactional
	public IssueBookResponseDTO issueBook(IssueBookRequestDTO dto) {

		User employee = getLoggedInUser();

		Book book = bookRepository.findById(dto.getBookId())
				.orElseThrow(() -> new ResourceNotFoundException("Book not found"));

		if (book.getAvailableCopies() <= 0) {

			throw new IllegalStateException("Book currently unavailable");
		}

		boolean pendingRequest = bookRequestRepository
				.findByUserIdAndBookBookIdAndRequestStatus(employee.getId(), book.getBookId(), "PENDING").isPresent();

		if (pendingRequest) {

			throw new IllegalStateException("Request already pending");
		}

		BookRequest request = BookRequest.builder().user(employee).book(book).requestStatus("PENDING").build();

		request = bookRequestRepository.save(request);

		List<User> admins = userRepository.findByRole("ADMIN");

		for (User admin : admins) {

			Notification notification = Notification
					.builder().user(admin).title("Book Request").message("Employee " + employee.getName() + " ("
							+ employee.getEmployeeId() + ") requested book '" + book.getTitle() + "'")
					.type("BOOK_REQUEST").build();

			notificationRepository.save(notification);
		}

		AuditLog auditLog = AuditLog.builder().userId(employee.getId()).userType("EMPLOYEE").actionType("BOOK_REQUEST")
				.actionDescription(employee.getName() + " requested " + book.getTitle()).build();

		auditLogRepository.save(auditLog);

		IssueBookResponseDTO response = new IssueBookResponseDTO();

		response.setRequestId(request.getRequestId());

		response.setBookId(book.getBookId());

		response.setTitle(book.getTitle());

		response.setRequestStatus(request.getRequestStatus());

		response.setMessage("Book request submitted successfully");

		return response;
	}

	private User getLoggedInUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

		return userDetails.getUser();
	}
}