package com.libr.mng.serviceImpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.libr.mng.dto.response.NotificationCountResponseDTO;
import com.libr.mng.dto.response.NotificationResponseDTO;
import com.libr.mng.entity.Notification;
import com.libr.mng.entity.User;
import com.libr.mng.exception.ResourceNotFoundException;
import com.libr.mng.repository.NotificationRepository;
import com.libr.mng.security.CustomUserDetails;
import com.libr.mng.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	private final NotificationRepository notificationRepository;
	private final ModelMapper modelMapper;

	private User getLoggedInUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

		return userDetails.getUser();
	}

	@Override
	public List<NotificationResponseDTO> getNotifications() {

		User user = getLoggedInUser();

		return notificationRepository.findByUserIdOrderByCreatedAtDesc(user.getId()).stream()
				.map(notification -> modelMapper.map(notification, NotificationResponseDTO.class)).toList();
	}

	@Override
	public NotificationCountResponseDTO getUnreadCount() {

		User user = getLoggedInUser();

		Long count = notificationRepository.countByUserIdAndIsReadFalse(user.getId());

		NotificationCountResponseDTO dto = new NotificationCountResponseDTO();

		dto.setUnreadCount(count);

		return dto;
	}

	@Override
	public String markAsRead(Long notificationId) {

		User user = getLoggedInUser();

		Notification notification = notificationRepository.findByNotificationIdAndUserId(notificationId, user.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

		notification.setIsRead(true);

		notificationRepository.save(notification);

		return "Notification marked as read";
	}

	@Override
	public String markAllAsRead() {

		User user = getLoggedInUser();

		List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(user.getId());

		notifications.forEach(notification -> notification.setIsRead(true));

		notificationRepository.saveAll(notifications);

		return "All notifications marked as read";
	}

	@Override
	public String deleteNotification(Long notificationId) {

		User user = getLoggedInUser();

		Notification notification = notificationRepository.findByNotificationIdAndUserId(notificationId, user.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

		notificationRepository.delete(notification);

		return "Notification deleted successfully";
	}
}