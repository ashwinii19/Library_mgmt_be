package com.libr.mng.service;

import java.util.List;

import com.libr.mng.dto.request.ReturnApprovalDTO;
import com.libr.mng.dto.request.ReturnBookRequestDTO;
import com.libr.mng.dto.response.ReturnBookResponseDTO;

public interface ReturnBookService {

	String requestReturn(Long issueId, ReturnBookRequestDTO dto);

	List<ReturnBookResponseDTO> getPendingReturns();

	String approveReturn(Long issueId, ReturnApprovalDTO dto);
}