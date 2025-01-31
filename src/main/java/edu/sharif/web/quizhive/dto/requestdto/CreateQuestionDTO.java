package edu.sharif.web.quizhive.dto.requestdto;

import edu.sharif.web.quizhive.model.Difficulty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateQuestionDTO {
	@NotBlank(message = "Question title is required")
	private String title;

	@NotBlank(message = "Question text is required")
	private String text;

	@NotNull(message = "Options are required")
	@Size(min = 4, max = 4, message = "There must be exactly 4 options")
	private List<@NotBlank(message = "Option cannot be blank") String> options;

	@Min(value = 0, message = "Correct answer index must be between 0 and 3")
	@Max(value = 3, message = "Correct answer index must be between 0 and 3")
	private int correct;

	@NotBlank(message = "Category ID is required")
	private String categoryId;

	@NotNull(message = "Difficulty level is required")
	private Difficulty difficulty;
}