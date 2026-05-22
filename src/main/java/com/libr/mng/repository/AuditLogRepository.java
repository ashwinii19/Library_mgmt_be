package com.libr.mng.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.libr.mng.entity.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

}