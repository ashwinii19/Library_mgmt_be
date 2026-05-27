package com.libr.mng.serviceImpl;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.libr.mng.dto.response.AnalyticsResponseDTO;
import com.libr.mng.entity.BookIssue;
import com.libr.mng.repository.BookIssueRepository;
import com.libr.mng.repository.BookRepository;
import com.libr.mng.repository.UserRepository;
import com.libr.mng.service.AnalyticsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

	private final BookRepository bookRepository;

	private final BookIssueRepository bookIssueRepository;

	private final UserRepository userRepository;

	@Override
	public AnalyticsResponseDTO getAnalytics() {

		AnalyticsResponseDTO dto = new AnalyticsResponseDTO();

		dto.setTotalBooks((long) bookRepository.findAll().size());

		dto.setTotalIssuedBooks((long) bookIssueRepository.findByIssueStatus("ISSUED").size());

		dto.setTotalReturnedBooks((long) bookIssueRepository.findByIssueStatus("RETURNED").size());

		dto.setTotalOverdueBooks((long) bookIssueRepository.findByIssueStatus("OVERDUE").size());

		dto.setActiveUsers(userRepository.count());

		Map<String, Long> bookCounts = bookIssueRepository.findAll().stream()
				.collect(Collectors.groupingBy(issue -> issue.getBook().getTitle(), Collectors.counting()));

		String mostBorrowedBook = bookCounts.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue))
				.map(Map.Entry::getKey).orElse("N/A");

		dto.setMostBorrowedBook(mostBorrowedBook);

		return dto;
	}
}