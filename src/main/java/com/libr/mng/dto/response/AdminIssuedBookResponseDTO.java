package com.libr.mng.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminIssuedBookResponseDTO {

	private Long issueId;

	private Long employeeId;

	private String employeeName;

	private String title;

	private String author;

	private String issueStatus;

	private LocalDate issueDate;

	private LocalDate dueDate;

	private LocalDate returnDate;
}