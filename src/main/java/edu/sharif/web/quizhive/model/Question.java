package edu.sharif.web.quizhive.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {
	@Id
	private String id;

	@NonNull
	private String title;

	private String text;

	@NonNull
	private List<String> options;

	@NonNull
	private int correct;

	@DBRef
	private Category category;

	private int solves;

	@NonNull
	private Difficulty difficulty;

	@NonNull
	private Date createdAt;

	// (not stored in DB)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private transient Integer lastChoiceByUser;

	@DBRef
	private User creator;
}