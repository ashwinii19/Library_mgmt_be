package com.libr.mng.serviceImpl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.libr.mng.dto.request.ApproveRejectRequestDTO;
import com.libr.mng.dto.response.AdminBookRequestResponseDTO;
import com.libr.mng.dto.response.AdminDashboardResponseDTO;
import com.libr.mng.entity.AuditLog;
import com.libr.mng.entity.Book;
import com.libr.mng.entity.BookIssue;
import com.libr.mng.entity.BookRequest;
import com.libr.mng.entity.LibraryPolicy;
import com.libr.mng.entity.Notification;
import com.libr.mng.entity.User;
import com.libr.mng.exception.ResourceNotFoundException;
import com.libr.mng.repository.AuditLogRepository;
import com.libr.mng.repository.BookIssueRepository;
import com.libr.mng.repository.BookRepository;
import com.libr.mng.repository.BookRequestRepository;
import com.libr.mng.repository.LibraryPolicyRepository;
import com.libr.mng.repository.NotificationRepository;
import com.libr.mng.security.CustomUserDetails;
import com.libr.mng.service.AdminService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

	private final BookRepository bookRepository;
	private final BookRequestRepository bookRequestRepository;
	private final BookIssueRepository bookIssueRepository;
	private final NotificationRepository notificationRepository;
	private final AuditLogRepository auditLogRepository;
	private final LibraryPolicyRepository libraryPolicyRepository;

	private User getLoggedInAdmin() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

		return userDetails.getUser();
	}

	@Override
	public AdminDashboardResponseDTO getDashboardStats() {

		AdminDashboardResponseDTO dto = new AdminDashboardResponseDTO();

		List<Book> books = bookRepository.findAll();

		dto.setTotalBooks((long) books.size());

		dto.setTotalCopies(books.stream().mapToLong(Book::getTotalCopies).sum());

		dto.setAvailableCopies(books.stream().mapToLong(Book::getAvailableCopies).sum());

		dto.setPendingRequests((long) bookRequestRepository.findByRequestStatus("PENDING").size());

		return dto;
	}

	@Override
	public List<AdminBookRequestResponseDTO> getPendingRequests() {

		return bookRequestRepository.findByRequestStatus("PENDING").stream().map(request -> {

			AdminBookRequestResponseDTO dto = new AdminBookRequestResponseDTO();

			dto.setRequestId(request.getRequestId());

			dto.setEmployeeId(request.getUser().getEmployeeId());

			dto.setEmployeeName(request.getUser().getName());

			dto.setBookId(request.getBook().getBookId());

			dto.setBookTitle(request.getBook().getTitle());

			dto.setRequestDate(request.getRequestDate());

			dto.setRequestStatus(request.getRequestStatus());

			return dto;

		}).toList();
	}

	@Override
	@Transactional
	public String approveRequest(Long requestId, ApproveRejectRequestDTO dto) {

		User admin = getLoggedInAdmin();

		BookRequest request = bookRequestRepository.findById(requestId)
				.orElseThrow(() -> new ResourceNotFoundException("Request not found"));

		if (!"PENDING".equalsIgnoreCase(request.getRequestStatus())) {

			throw new IllegalStateException("Request already processed");
		}

		Book book = request.getBook();

		if (book.getAvailableCopies() <= 0) {

			throw new IllegalStateException("Book unavailable");
		}

		request.setRequestStatus("APPROVED");
		request.setAdminRemarks(dto.getAdminRemarks());
		request.setProcessedDate(LocalDate.now());

		bookRequestRepository.save(request);

		BookIssue issue = BookIssue.builder().user(request.getUser()).book(book).issuedBy(admin)
				.issueDate(LocalDate.now()).dueDate(LocalDate.now().plusDays(45)).issueStatus("ISSUED").renewalCount(0)
				.build();

		bookIssueRepository.save(issue);

		book.setAvailableCopies(book.getAvailableCopies() - 1);

		if (book.getAvailableCopies() == 0) {

			book.setBookStatus("OUT_OF_STOCK");

		} else {

			book.setBookStatus("AVAILABLE");
		}

		bookRepository.save(book);

		Notification notification = Notification
				.builder().user(request.getUser()).title("Book Request Approved").message("Your request for book '"
						+ book.getTitle() + "' has been approved. " + "Please return it within 45 days.")
				.type("REQUEST_APPROVED").build();

		notificationRepository.save(notification);

		AuditLog auditLog = AuditLog.builder().userId(admin.getId()).userType("ADMIN").actionType("APPROVE_REQUEST")
				.actionDescription(admin.getName() + " approved request " + requestId + " for book " + book.getTitle())
				.build();

		auditLogRepository.save(auditLog);

		return "Book request approved successfully";
	}

	@Override
	@Transactional
	public String rejectRequest(Long requestId, ApproveRejectRequestDTO dto) {

		User admin = getLoggedInAdmin();

		BookRequest request = bookRequestRepository.findById(requestId)
				.orElseThrow(() -> new ResourceNotFoundException("Request not found"));

		if (!"PENDING".equalsIgnoreCase(request.getRequestStatus())) {

			throw new IllegalStateException("Request already processed");
		}

		request.setRequestStatus("REJECTED");
		request.setAdminRemarks(dto.getAdminRemarks());
		request.setProcessedDate(LocalDate.now());

		bookRequestRepository.save(request);

		Notification notification = Notification.builder().user(request.getUser()).title("Book Request Rejected")
				.message("Your request for book '" + request.getBook().getTitle() + "' has been rejected. "
						+ "You may add it to wishlist and " + "get notified when it becomes available.")
				.type("REQUEST_REJECTED").build();

		notificationRepository.save(notification);

		AuditLog auditLog = AuditLog.builder().userId(admin.getId()).userType("ADMIN").actionType("REJECT_REQUEST")
				.actionDescription(admin.getName() + " rejected request " + requestId + " for book "
						+ request.getBook().getTitle())
				.build();

		auditLogRepository.save(auditLog);

		return "Book request rejected successfully";
	}
}