package edu.sharif.web.quizhive.dto.resultdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubmitDTO {
	private String id;
	private String questionId;
	private int choice;
	private boolean isCorrect;
	private int gainedScore;
	private long timestamp;
}
