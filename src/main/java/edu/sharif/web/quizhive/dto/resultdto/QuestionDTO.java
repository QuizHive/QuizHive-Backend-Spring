package edu.sharif.web.quizhive.dto.resultdto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDTO {
	private String id;
	private String title;
	private String text;
	private List<String> options;
	private int correct;
	private CategoryDTO category;
	private String creator;
	@Nullable
	private int lastChoiceByUser;
	private int solves;
	private int difficulty;
}
