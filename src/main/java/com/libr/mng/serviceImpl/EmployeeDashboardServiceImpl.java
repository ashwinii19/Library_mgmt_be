package com.libr.mng.serviceImpl;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.libr.mng.dto.response.EmployeeDashboardDataResponseDTO;
import com.libr.mng.dto.response.EmployeeDashboardResponseDTO;
import com.libr.mng.dto.response.NotificationResponseDTO;
import com.libr.mng.dto.response.QuickActionResponseDTO;
import com.libr.mng.dto.response.RecentActivityResponseDTO;
import com.libr.mng.entity.BookIssue;
import com.libr.mng.entity.User;
import com.libr.mng.repository.BookIssueRepository;
import com.libr.mng.repository.BookRepository;
import com.libr.mng.repository.NotificationRepository;
import com.libr.mng.repository.WishlistRepository;
import com.libr.mng.security.CustomUserDetails;
import com.libr.mng.service.EmployeeDashboardService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeDashboardServiceImpl implements EmployeeDashboardService {

	private final BookRepository bookRepository;

	private final BookIssueRepository bookIssueRepository;

	private final WishlistRepository wishlistRepository;

	private final NotificationRepository notificationRepository;

	private final ModelMapper modelMapper;

	@Override
	public EmployeeDashboardDataResponseDTO getDashboard() {

		User user = getLoggedInUser();

		Page<BookIssue> issuedPage = bookIssueRepository.findByUserIdOrderByIssueDateDesc(user.getId(),
				PageRequest.of(0, 100));

		List<BookIssue> issues = issuedPage.getContent();

		EmployeeDashboardResponseDTO dashboard = EmployeeDashboardResponseDTO.builder()

				.totalBooksAvailable(
						bookRepository.findAll().stream().mapToLong(book -> book.getAvailableCopies()).sum())

				.booksIssued((long) issues.stream().filter(issue -> "ISSUED".equalsIgnoreCase(issue.getIssueStatus()))
						.count())

				.overdueBooks((long) issues.stream().filter(issue -> "OVERDUE".equalsIgnoreCase(issue.getIssueStatus()))
						.count())

				.wishlistCount((long) wishlistRepository.findByUserId(user.getId()).size())

				.unreadNotifications(notificationRepository.countByUserIdAndIsReadFalse(user.getId()))

				.build();

		List<RecentActivityResponseDTO> activities = buildRecentActivities(user);

		List<NotificationResponseDTO> unreadNotifications = notificationRepository
				.findByUserIdOrderByCreatedAtDesc(user.getId(), PageRequest.of(0, 5)).getContent().stream()
				.filter(notification -> !notification.getIsRead())
				.map(notification -> modelMapper.map(notification, NotificationResponseDTO.class)).toList();

		List<QuickActionResponseDTO> quickActions = buildQuickActions();

		return EmployeeDashboardDataResponseDTO.builder().dashboard(dashboard).recentActivities(activities)
				.unreadNotifications(unreadNotifications).quickActions(quickActions).build();
	}

	private List<RecentActivityResponseDTO> buildRecentActivities(User user) {

		List<RecentActivityResponseDTO> activities = new ArrayList<>();

		Page<BookIssue> issuePage = bookIssueRepository.findByUserIdOrderByIssueDateDesc(user.getId(),
				PageRequest.of(0, 5));

		List<BookIssue> issues = issuePage.getContent();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

		for (BookIssue issue : issues) {

			RecentActivityResponseDTO dto = new RecentActivityResponseDTO();

			dto.setBookTitle(issue.getBook().getTitle());

			dto.setActivityType(issue.getIssueStatus());

			if ("ISSUED".equalsIgnoreCase(issue.getIssueStatus())) {

				dto.setMessage("Book issued successfully");

			} else if ("RETURNED".equalsIgnoreCase(issue.getIssueStatus())) {

				dto.setMessage("Book returned");

			} else if ("OVERDUE".equalsIgnoreCase(issue.getIssueStatus())) {

				dto.setMessage("Book overdue");
			}

			dto.setActivityDate(issue.getIssueDate().format(formatter));

			activities.add(dto);
		}

		return activities;
	}

	private List<QuickActionResponseDTO> buildQuickActions() {

		List<QuickActionResponseDTO> actions = new ArrayList<>();

		actions.add(QuickActionResponseDTO.builder().actionName("Browse Inventory").actionUrl("/inventory").build());

		actions.add(
				QuickActionResponseDTO.builder().actionName("View Issued Books").actionUrl("/issued-books").build());

		actions.add(QuickActionResponseDTO.builder().actionName("Manage Wishlist").actionUrl("/wishlist").build());

		return actions;
	}

	private User getLoggedInUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

		return userDetails.getUser();
	}
}