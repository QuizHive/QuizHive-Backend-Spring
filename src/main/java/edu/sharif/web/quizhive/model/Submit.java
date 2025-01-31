package edu.sharif.web.quizhive.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "submits")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Submit {
	@Id
	private String id;
	@DBRef
	private Question question;
	@DBRef
	private User user;
	@NonNull
	private int choice;
	@NonNull
	private boolean isCorrect;
	@NonNull
	private int gainedScore;
	@NonNull
	private Date timestamp;
}
