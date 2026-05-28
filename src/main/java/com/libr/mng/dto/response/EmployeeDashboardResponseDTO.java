package com.libr.mng.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDashboardResponseDTO {

	private Long totalBooksAvailable;

	private Long booksIssued;

	private Long overdueBooks;

	private Long wishlistCount;

	private Long unreadNotifications;
}