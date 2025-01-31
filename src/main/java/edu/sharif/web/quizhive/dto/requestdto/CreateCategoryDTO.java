package edu.sharif.web.quizhive.dto.requestdto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCategoryDTO {
	@NotBlank(message = "Category name is required")
	private String categoryName;

	@NotBlank(message = "Description is required")
	private String description;
}