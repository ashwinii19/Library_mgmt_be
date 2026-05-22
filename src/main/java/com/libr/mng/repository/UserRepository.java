package com.libr.mng.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.libr.mng.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);

	boolean existsByEmployeeId(Long employeeId);

	List<User> findByRole(String role);
}