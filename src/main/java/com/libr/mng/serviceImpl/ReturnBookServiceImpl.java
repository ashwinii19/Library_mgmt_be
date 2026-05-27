package com.libr.mng.serviceImpl;

import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.libr.mng.dto.request.ReturnApprovalDTO;
import com.libr.mng.dto.request.ReturnBookRequestDTO;
import com.libr.mng.dto.response.ReturnBookResponseDTO;
import com.libr.mng.entity.*;
import com.libr.mng.exception.ResourceNotFoundException;
import com.libr.mng.repository.*;
import com.libr.mng.security.CustomUserDetails;
import com.libr.mng.service.ReturnBookService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReturnBookServiceImpl implements ReturnBookService {

	private final BookIssueRepository bookIssueRepository;
	private final BookRepository bookRepository;
	private final BookReturnRepository bookReturnRepository;
	private final NotificationRepository notificationRepository;
	private final AuditLogRepository auditLogRepository;
	private final ModelMapper modelMapper;

	private User getLoggedInUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

		return userDetails.getUser();
	}

	@Override
	public String requestReturn(Long issueId, ReturnBookRequestDTO dto) {

		User employee = getLoggedInUser();

		BookIssue issue = bookIssueRepository.findById(issueId)
				.orElseThrow(() -> new ResourceNotFoundException("Issue not found"));

		if (!issue.getUser().getId().equals(employee.getId())) {

			throw new IllegalStateException("Unauthorized");
		}

		if ("RETURNED".equalsIgnoreCase(issue.getIssueStatus())) {

			throw new IllegalStateException("Book already returned");
		}

		List<User> admins = issue.getBook().getBookRequests().stream().map(BookRequest::getUser)
				.filter(user -> "ADMIN".equalsIgnoreCase(user.getRole())).toList();

		notificationRepository.save(Notification.builder().user(issue.getIssuedBy()).title("Return Request")
				.message(employee.getName() + " requested return for book '" + issue.getBook().getTitle() + "'")
				.type("RETURN_REQUEST").build());

		return "Return request submitted";
	}

	@Override
	public List<ReturnBookResponseDTO> getPendingReturns() {

		return bookIssueRepository.findAll().stream()
				.filter(issue -> !"RETURNED".equalsIgnoreCase(issue.getIssueStatus())).map(this::mapToDto).toList();
	}

	@Override
	@Transactional
	public String approveReturn(Long issueId, ReturnApprovalDTO dto) {

		User admin = getLoggedInUser();

		BookIssue issue = bookIssueRepository.findById(issueId)
				.orElseThrow(() -> new ResourceNotFoundException("Issue not found"));

		if ("RETURNED".equalsIgnoreCase(issue.getIssueStatus())) {

			throw new IllegalStateException("Book already returned");
		}

		issue.setIssueStatus("RETURNED");
		issue.setReturnDate(LocalDate.now());

		bookIssueRepository.save(issue);

		Book book = issue.getBook();

		book.setAvailableCopies(book.getAvailableCopies() + 1);

		if (book.getAvailableCopies() > 0) {

			book.setBookStatus("AVAILABLE");
		}

		bookRepository.save(book);

		BookReturn bookReturn = BookReturn.builder().bookIssue(issue).processedBy(admin).returnDate(LocalDate.now())
				.returnCondition(dto.getReturnCondition()).remarks(dto.getRemarks()).build();

		bookReturnRepository.save(bookReturn);

		notificationRepository.save(Notification.builder().user(issue.getUser()).title("Book Returned")
				.message("Your returned book '" + book.getTitle() + "' has been accepted").type("RETURN_APPROVED")
				.build());

		auditLogRepository.save(AuditLog.builder().userId(admin.getId()).userType("ADMIN").actionType("RETURN_BOOK")
				.actionDescription(admin.getName() + " accepted return for " + book.getTitle()).build());

		return "Book returned successfully";
	}

	private ReturnBookResponseDTO mapToDto(BookIssue issue) {

		ReturnBookResponseDTO dto = new ReturnBookResponseDTO();

		dto.setIssueId(issue.getIssueId());

		dto.setBookTitle(issue.getBook().getTitle());

		dto.setEmployeeName(issue.getUser().getName());

		dto.setIssueDate(issue.getIssueDate());

		dto.setDueDate(issue.getDueDate());

		dto.setReturnDate(issue.getReturnDate());

		dto.setIssueStatus(issue.getIssueStatus());

		return dto;
	}
}