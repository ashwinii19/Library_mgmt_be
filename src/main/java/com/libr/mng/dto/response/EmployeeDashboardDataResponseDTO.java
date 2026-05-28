package com.libr.mng.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDashboardDataResponseDTO {

	private EmployeeDashboardResponseDTO dashboard;

	private List<RecentActivityResponseDTO> recentActivities;

	private List<NotificationResponseDTO> unreadNotifications;

	private List<QuickActionResponseDTO> quickActions;
}