package com.libr.mng.serviceImpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.libr.mng.dto.response.AdminIssuedBookResponseDTO;
import com.libr.mng.entity.BookIssue;
import com.libr.mng.repository.BookIssueRepository;
import com.libr.mng.service.AdminMonitoringService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminMonitoringServiceImpl implements AdminMonitoringService {

	private final BookIssueRepository bookIssueRepository;

	private final ModelMapper modelMapper;

	@Override
	public List<AdminIssuedBookResponseDTO> getAllIssuedBooks() {

		Pageable pageable = PageRequest.of(0, 10);

		Page<BookIssue> issuedBooks = bookIssueRepository.findAll(pageable);

		return issuedBooks.getContent().stream().map(this::mapToDto).toList();
	}

	@Override
	public List<AdminIssuedBookResponseDTO> getOverdueBooks() {

		Pageable pageable = PageRequest.of(0, 10);

		Page<BookIssue> overdueBooks = bookIssueRepository.findByIssueStatus("OVERDUE", pageable);

		return overdueBooks.getContent().stream().map(this::mapToDto).toList();
	}

	@Override
	public List<AdminIssuedBookResponseDTO> getReturnedBooks() {

		Pageable pageable = PageRequest.of(0, 10);

		Page<BookIssue> returnedBooks = bookIssueRepository.findByIssueStatus("RETURNED", pageable);

		return returnedBooks.getContent().stream().map(this::mapToDto).toList();
	}

	private AdminIssuedBookResponseDTO mapToDto(BookIssue issue) {

		AdminIssuedBookResponseDTO dto = modelMapper.map(issue, AdminIssuedBookResponseDTO.class);

		dto.setEmployeeId(issue.getUser().getEmployeeId());

		dto.setEmployeeName(issue.getUser().getName());

		dto.setTitle(issue.getBook().getTitle());

		dto.setAuthor(issue.getBook().getAuthor());

		return dto;
	}
}