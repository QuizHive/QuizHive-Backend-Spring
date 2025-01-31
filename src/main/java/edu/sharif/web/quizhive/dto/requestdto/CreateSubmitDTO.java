package edu.sharif.web.quizhive.dto.requestdto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSubmitDTO {
	@NotBlank(message = "Question ID is required")
	private String questionId;

	@Min(value = 0, message = "Choice index must be between 0 and 3")
	@Max(value = 3, message = "Choice index must be between 0 and 3")
	private int choice;
}
