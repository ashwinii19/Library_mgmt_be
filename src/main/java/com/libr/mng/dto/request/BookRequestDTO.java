package com.libr.mng.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookRequestDTO {

	@NotBlank(message = "Title is required")
	private String title;

	@NotBlank(message = "Author is required")
	private String author;

	@NotBlank(message = "ISBN is required")
	private String isbnNumber;

	@NotBlank(message = "Category is required")
	private String category;

	private String publisher;

	@NotNull(message = "Publication year is required")
	private Integer publicationYear;

	private String description;

	@NotNull(message = "Total copies required")
	@Min(value = 1)
	private Integer totalCopies;
}
