package com.libr.mng.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecentActivityResponseDTO {

	private String activityType;

	private String bookTitle;

	private String message;

	private String activityDate;
}