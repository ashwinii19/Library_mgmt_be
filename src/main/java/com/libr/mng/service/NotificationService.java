package com.libr.mng.service;

import java.util.List;

import com.libr.mng.dto.response.NotificationCountResponseDTO;
import com.libr.mng.dto.response.NotificationResponseDTO;

public interface NotificationService {

	List<NotificationResponseDTO> getNotifications();

	NotificationCountResponseDTO getUnreadCount();

	String markAsRead(Long notificationId);

	String markAllAsRead();

	String deleteNotification(Long notificationId);
}