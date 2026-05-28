package com.libr.mng.scheduler;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.libr.mng.entity.BookIssue;
import com.libr.mng.entity.Notification;
import com.libr.mng.repository.BookIssueRepository;
import com.libr.mng.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SchedulerService {

	private final BookIssueRepository bookIssueRepository;

	private final NotificationRepository notificationRepository;

	@Scheduled(cron = "0 0 9 * * *")
	public void sendDueReminderNotifications() {

		LocalDate today = LocalDate.now();

		List<BookIssue> issuedBooks = bookIssueRepository.findByIssueStatus("ISSUED", PageRequest.of(0, 1000))
				.getContent();

		for (BookIssue issue : issuedBooks) {

			long daysLeft = ChronoUnit.DAYS.between(today, issue.getDueDate());

			if (daysLeft == 2) {

				boolean alreadySent = notificationRepository.existsByUserIdAndTitleAndType(issue.getUser().getId(),
						"Book Due Reminder", "DUE_REMINDER");

				if (!alreadySent) {

					notificationRepository.save(

							Notification.builder().user(issue.getUser()).title("Book Due Reminder")
									.message("Only 48 hours left to return book '" + issue.getBook().getTitle() + "'")
									.type("DUE_REMINDER").build());
				}
			}

			if (daysLeft == 1) {

				boolean alreadySent = notificationRepository.existsByUserIdAndTitleAndType(issue.getUser().getId(),
						"Urgent Return Reminder", "URGENT_REMINDER");

				if (!alreadySent) {

					notificationRepository.save(

							Notification.builder().user(issue.getUser()).title("Urgent Return Reminder")
									.message("Only 24 hours left to return book '" + issue.getBook().getTitle() + "'")
									.type("URGENT_REMINDER").build());
				}
			}

			if (daysLeft < 0 && !"OVERDUE".equalsIgnoreCase(issue.getIssueStatus())) {

				issue.setIssueStatus("OVERDUE");

				bookIssueRepository.save(issue);

				boolean alreadySent = notificationRepository.existsByUserIdAndTitleAndType(issue.getUser().getId(),
						"Book Overdue", "OVERDUE");

				if (!alreadySent) {

					notificationRepository.save(

							Notification.builder().user(issue.getUser()).title("Book Overdue")
									.message("Book '" + issue.getBook().getTitle() + "' is overdue").type("OVERDUE")
									.build());
				}
			}
		}
	}
}