package com.libr.mng.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticsResponseDTO {

	private Long totalBooks;

	private Long totalIssuedBooks;

	private Long totalReturnedBooks;

	private Long totalOverdueBooks;

	private Long activeUsers;

	private String mostBorrowedBook;
}