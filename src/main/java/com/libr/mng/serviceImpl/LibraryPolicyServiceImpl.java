package com.libr.mng.serviceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.libr.mng.dto.request.LibraryPolicyRequestDTO;
import com.libr.mng.dto.response.LibraryPolicyResponseDTO;
import com.libr.mng.entity.LibraryPolicy;
import com.libr.mng.exception.ResourceNotFoundException;
import com.libr.mng.repository.LibraryPolicyRepository;
import com.libr.mng.service.LibraryPolicyService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LibraryPolicyServiceImpl implements LibraryPolicyService {

	private final LibraryPolicyRepository libraryPolicyRepository;

	private final ModelMapper modelMapper;

	@Override
	public LibraryPolicyResponseDTO createPolicy(LibraryPolicyRequestDTO dto) {

		if (!libraryPolicyRepository.findAll().isEmpty()) {

			throw new IllegalStateException("Policy already exists");
		}

		LibraryPolicy policy = modelMapper.map(dto, LibraryPolicy.class);

		LibraryPolicy savedPolicy = libraryPolicyRepository.save(policy);

		return modelMapper.map(savedPolicy, LibraryPolicyResponseDTO.class);
	}

	@Override
	public LibraryPolicyResponseDTO updatePolicy(Long policyId, LibraryPolicyRequestDTO dto) {

		LibraryPolicy policy = libraryPolicyRepository.findById(policyId)
				.orElseThrow(() -> new ResourceNotFoundException("Policy not found"));

		if (dto.getMaxBorrowDays() != null) {

			policy.setMaxBorrowDays(dto.getMaxBorrowDays());
		}

		if (dto.getMaxBooksAllowed() != null) {

			policy.setMaxBooksAllowed(dto.getMaxBooksAllowed());
		}

		if (dto.getLibraryOpenTime() != null) {

			policy.setLibraryOpenTime(dto.getLibraryOpenTime());
		}

		if (dto.getLibraryCloseTime() != null) {

			policy.setLibraryCloseTime(dto.getLibraryCloseTime());
		}

		if (dto.getLateFinePerDay() != null) {

			policy.setLateFinePerDay(dto.getLateFinePerDay());
		}

		if (dto.getSuspensionDays() != null) {

			policy.setSuspensionDays(dto.getSuspensionDays());
		}

		LibraryPolicy updatedPolicy = libraryPolicyRepository.save(policy);

		return modelMapper.map(updatedPolicy, LibraryPolicyResponseDTO.class);
	}

	@Override
	public LibraryPolicyResponseDTO getPolicy() {

		LibraryPolicy policy = libraryPolicyRepository.findAll().stream().findFirst()
				.orElseThrow(() -> new ResourceNotFoundException("Library policy not found"));

		return modelMapper.map(policy, LibraryPolicyResponseDTO.class);
	}
}