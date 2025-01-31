package edu.sharif.web.quizhive.dto.requestdto;

import edu.sharif.web.quizhive.model.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetQuestionsDTO {
	private String categoryId;
	private Difficulty difficulty;
	private boolean followedCreator;
	private int limit;
}
