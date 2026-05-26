package com.libr.mng.serviceImpl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.modelmapper.ModelMapper;
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

		return bookIssueRepository.findByUserIdOrderByIssueDateDesc(user.getId()).stream().map(this::mapToDto).toList();
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

		LocalDate today = LocalDate.now();

		long daysRemaining = ChronoUnit.DAYS.between(today, issue.getDueDate());

		if (daysRemaining >= 0) {

			dto.setDaysRemaining(daysRemaining);

			dto.setOverdue(false);

			dto.setOverdueDays(0L);

		} else {

			dto.setDaysRemaining(0L);

			dto.setOverdue(true);

			dto.setOverdueDays(Math.abs(daysRemaining));

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

			dto.setRemainingHours(totalSeconds / 3600);

			dto.setRemainingMinutes((totalSeconds % 3600) / 60);

			dto.setRemainingSeconds(totalSeconds % 60);

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