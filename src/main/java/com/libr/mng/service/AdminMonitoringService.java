package com.libr.mng.service;

import java.util.List;

import com.libr.mng.dto.response.AdminIssuedBookResponseDTO;

public interface AdminMonitoringService {

	List<AdminIssuedBookResponseDTO> getAllIssuedBooks();

	List<AdminIssuedBookResponseDTO> getOverdueBooks();

	List<AdminIssuedBookResponseDTO> getReturnedBooks();
}