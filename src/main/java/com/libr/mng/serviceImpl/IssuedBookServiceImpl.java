package com.libr.mng.serviceImpl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.libr.mng.dto.response.IssuedBookResponseDTO;
import com.libr.mng.entity.BookIssue;
import com.libr.mng.entity.User;
import com.libr.mng.repository.BookIssueRepository;
import com.libr.mng.security.CustomUserDetails;
import com.libr.mng.service.IssuedBookService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IssuedBookServiceImpl implements IssuedBookService {

	private final BookIssueRepository bookIssueRepository;

	private final ModelMapper modelMapper;

	@Override
	public List<IssuedBookResponseDTO> getMyIssuedBooks() {

		User user = getLoggedInUser();

		Pageable pageable = PageRequest.of(0, 10);

		Page<BookIssue> issuedBooks = bookIssueRepository.findByUserIdOrderByIssueDateDesc(user.getId(), pageable);

		return issuedBooks.getContent().stream().map(this::mapToDto).toList();
	}

	private IssuedBookResponseDTO mapToDto(BookIssue issue) {

		IssuedBookResponseDTO dto = modelMapper.map(issue, IssuedBookResponseDTO.class);

		dto.setBookId(issue.getBook().getBookId());

		dto.setTitle(issue.getBook().getTitle());

		dto.setAuthor(issue.getBook().getAuthor());

		dto.setImageUrl(issue.getBook().getImageUrl());

		dto.setRenewalAllowed(issue.getRenewalCount() < 1 && "ISSUED".equalsIgnoreCase(issue.getIssueStatus()));

		dto.setReturnAllowed("ISSUED".equalsIgnoreCase(issue.getIssueStatus())
				|| "OVERDUE".equalsIgnoreCase(issue.getIssueStatus()));

		long remainingDays = ChronoUnit.DAYS.between(LocalDate.now(), issue.getDueDate());

		dto.setRemainingDays(remainingDays);

		dto.setCountdownEnabled(remainingDays <= 2 && remainingDays >= 0);

		if (remainingDays == 2) {

			dto.setCountdownMessage("48 Hours Remaining");

		} else if (remainingDays == 1) {

			dto.setCountdownMessage("24 Hours Remaining");

		} else if (remainingDays == 0) {

			dto.setCountdownMessage("Due Today");

		} else if (remainingDays < 0) {

			dto.setCountdownMessage("Overdue");
		}

		if (remainingDays >= 0) {

			dto.setOverdue(false);

			dto.setOverdueDays(0L);

		} else {

			dto.setOverdue(true);

			dto.setOverdueDays(Math.abs(remainingDays));

			dto.setCountdownNote("This book is overdue. Please return it immediately.");
		}

		calculateCountdown(dto, issue);

		return dto;
	}

	private void calculateCountdown(IssuedBookResponseDTO dto, BookIssue issue) {

		LocalDateTime dueDateTime = LocalDateTime.of(issue.getDueDate(), LocalTime.of(23, 59, 59));

		LocalDateTime now = LocalDateTime.now();

		Duration duration = Duration.between(now, dueDateTime);

		long totalSeconds = duration.getSeconds();

		if (totalSeconds <= 172800 && totalSeconds > 0) {

			dto.setShowCountdown(true);

			long hours = totalSeconds / 3600;

			long minutes = (totalSeconds % 3600) / 60;

			long seconds = totalSeconds % 60;

			dto.setRemainingHours(hours);

			dto.setRemainingMinutes(minutes);

			dto.setRemainingSeconds(seconds);

			if (hours >= 24) {

				dto.setCountdownNote("48 hours remaining to return this book");

			} else {

				dto.setCountdownNote("24 hours remaining to return this book");
			}

		} else {

			dto.setShowCountdown(false);

			dto.setRemainingHours(0L);

			dto.setRemainingMinutes(0L);

			dto.setRemainingSeconds(0L);
		}
	}

	private User getLoggedInUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

		return userDetails.getUser();
	}
}