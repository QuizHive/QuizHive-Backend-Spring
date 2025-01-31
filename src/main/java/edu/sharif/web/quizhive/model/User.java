package edu.sharif.web.quizhive.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Document(collection = "users")
public class User {
	@Id
	private String id;

	@NonNull
	private String email;

	@NonNull
	private String passHash;

	@NonNull
	private String nickname;

	@NonNull
	private Role role;

	@NonNull
	private int score;

	@NonNull
	private Date createdAt;

	@DBRef(lazy = true)
	private List<User> followings;
}