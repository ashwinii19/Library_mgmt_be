package com.libr.mng.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.libr.mng.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}