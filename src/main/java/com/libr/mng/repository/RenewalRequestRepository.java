package com.libr.mng.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.libr.mng.entity.RenewalRequest;

public interface RenewalRequestRepository extends JpaRepository<RenewalRequest, Long> {

	List<RenewalRequest> findByRequestStatus(String status);

	Optional<RenewalRequest> findByBookIssueIssueIdAndRequestStatus(Long issueId, String requestStatus);

	List<RenewalRequest> findByUserId(Long userId);
}