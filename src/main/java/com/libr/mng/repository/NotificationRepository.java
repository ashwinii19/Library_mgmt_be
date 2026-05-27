package com.libr.mng.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.libr.mng.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

	Long countByUserIdAndIsReadFalse(Long userId);

	Optional<Notification> findByNotificationIdAndUserId(Long notificationId, Long userId);
}