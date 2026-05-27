package com.libr.mng.service;

import java.util.List;

import com.libr.mng.dto.request.RenewalRequestDTO;
import com.libr.mng.dto.request.RenewalApprovalDTO;
import com.libr.mng.dto.response.RenewalResponseDTO;

public interface RenewalService {

	String requestRenewal(Long issueId, RenewalRequestDTO dto);

	List<RenewalResponseDTO> getMyRenewalRequests();

	List<RenewalResponseDTO> getPendingRenewalRequests();

	String approveRenewal(Long renewalId, RenewalApprovalDTO dto);

	String rejectRenewal(Long renewalId, RenewalApprovalDTO dto);
}