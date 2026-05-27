package com.libr.mng.service;

import com.libr.mng.dto.request.LibraryPolicyRequestDTO;
import com.libr.mng.dto.response.LibraryPolicyResponseDTO;

public interface LibraryPolicyService {

	LibraryPolicyResponseDTO createPolicy(LibraryPolicyRequestDTO dto);

	LibraryPolicyResponseDTO updatePolicy(Long policyId, LibraryPolicyRequestDTO dto);

	LibraryPolicyResponseDTO getPolicy();
}