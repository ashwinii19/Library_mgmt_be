package com.libr.mng.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.libr.mng.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	Page<Notification> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);	

	Long countByUserIdAndIsReadFalse(Long userId);

	Optional<Notification> findByNotificationIdAndUserId(Long notificationId, Long userId);

	boolean existsByUserIdAndTitleAndType(Long userId, String title, String type);
}