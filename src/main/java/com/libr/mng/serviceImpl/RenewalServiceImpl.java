package com.libr.mng.serviceImpl;

import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.libr.mng.dto.request.RenewalApprovalDTO;
import com.libr.mng.dto.request.RenewalRequestDTO;
import com.libr.mng.dto.response.RenewalResponseDTO;
import com.libr.mng.entity.*;
import com.libr.mng.exception.ResourceNotFoundException;
import com.libr.mng.repository.*;
import com.libr.mng.security.CustomUserDetails;
import com.libr.mng.service.RenewalService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RenewalServiceImpl implements RenewalService {

	private final RenewalRequestRepository renewalRequestRepository;
	private final BookIssueRepository bookIssueRepository;
	private final NotificationRepository notificationRepository;
	private final AuditLogRepository auditLogRepository;
	private final ModelMapper modelMapper;

	private User getLoggedInUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

		return userDetails.getUser();
	}

	@Override
	@Transactional
	public String requestRenewal(Long issueId, RenewalRequestDTO dto) {

		User employee = getLoggedInUser();

		BookIssue issue = bookIssueRepository.findById(issueId)
				.orElseThrow(() -> new ResourceNotFoundException("Issue not found"));

		if (!issue.getUser().getId().equals(employee.getId())) {

			throw new IllegalStateException("Unauthorized");
		}

		if (issue.getRenewalCount() >= 1) {

			throw new IllegalStateException("Renewal already used");
		}

		boolean exists = renewalRequestRepository.findByBookIssueIssueIdAndRequestStatus(issueId, "PENDING")
				.isPresent();

		if (exists) {

			throw new IllegalStateException("Renewal request already pending");
		}

		RenewalRequest request = RenewalRequest.builder().bookIssue(issue).user(employee).requestStatus("PENDING")
				.adminRemarks(dto.getRemarks()).build();

		renewalRequestRepository.save(request);

		return "Renewal request submitted";
	}

	@Override
	public List<RenewalResponseDTO> getMyRenewalRequests() {

		User user = getLoggedInUser();

		return renewalRequestRepository.findByUserId(user.getId()).stream().map(this::mapToDto).toList();
	}

	@Override
	public List<RenewalResponseDTO> getPendingRenewalRequests() {

		return renewalRequestRepository.findByRequestStatus("PENDING").stream().map(this::mapToDto).toList();
	}

	@Override
	@Transactional
	public String approveRenewal(Long renewalId, RenewalApprovalDTO dto) {

		RenewalRequest renewal = renewalRequestRepository.findById(renewalId)
				.orElseThrow(() -> new ResourceNotFoundException("Renewal not found"));

		if (!"PENDING".equalsIgnoreCase(renewal.getRequestStatus())) {

			throw new IllegalStateException("Already processed");
		}

		BookIssue issue = renewal.getBookIssue();

		LocalDate newDueDate = issue.getDueDate().plusDays(15);

		issue.setDueDate(newDueDate);
		issue.setRenewalCount(1);

		bookIssueRepository.save(issue);

		renewal.setRequestStatus("APPROVED");
		renewal.setApprovedNewDueDate(newDueDate);
		renewal.setExtendedDueDate(newDueDate);
		renewal.setAdminRemarks(dto.getAdminRemarks());

		renewalRequestRepository.save(renewal);

		notificationRepository.save(Notification.builder().user(issue.getUser()).title("Renewal Approved")
				.message("Book renewed for 15 more days").type("RENEWAL_APPROVED").build());

		return "Renewal approved";
	}

	@Override
	@Transactional
	public String rejectRenewal(Long renewalId, RenewalApprovalDTO dto) {

		RenewalRequest renewal = renewalRequestRepository.findById(renewalId)
				.orElseThrow(() -> new ResourceNotFoundException("Renewal not found"));

		renewal.setRequestStatus("REJECTED");
		renewal.setAdminRemarks(dto.getAdminRemarks());

		renewalRequestRepository.save(renewal);

		notificationRepository.save(Notification.builder().user(renewal.getUser()).title("Renewal Rejected")
				.message("Your renewal request was rejected").type("RENEWAL_REJECTED").build());

		return "Renewal rejected";
	}

	private RenewalResponseDTO mapToDto(RenewalRequest request) {

		RenewalResponseDTO dto = modelMapper.map(request, RenewalResponseDTO.class);

		dto.setIssueId(request.getBookIssue().getIssueId());

		dto.setBookTitle(request.getBookIssue().getBook().getTitle());

		dto.setEmployeeName(request.getUser().getName());

		return dto;
	}
}