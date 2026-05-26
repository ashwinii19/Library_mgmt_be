package com.libr.mng.service;

import java.util.List;

import com.libr.mng.dto.request.ApproveRejectRequestDTO;
import com.libr.mng.dto.response.AdminBookRequestResponseDTO;
import com.libr.mng.dto.response.AdminDashboardResponseDTO;

public interface AdminService {

	AdminDashboardResponseDTO getDashboardStats();

	List<AdminBookRequestResponseDTO> getPendingRequests();

	String approveRequest(Long requestId, ApproveRejectRequestDTO dto);

	String rejectRequest(Long requestId, ApproveRejectRequestDTO dto);
}