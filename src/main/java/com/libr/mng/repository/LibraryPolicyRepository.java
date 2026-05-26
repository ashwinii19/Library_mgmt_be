package com.libr.mng.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.libr.mng.entity.LibraryPolicy;

public interface LibraryPolicyRepository extends JpaRepository<LibraryPolicy, Long> {

}