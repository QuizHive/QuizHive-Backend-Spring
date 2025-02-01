package edu.sharif.web.quizhive.dto.requestdto;

import edu.sharif.web.quizhive.model.Role;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchUserDTO {
	@Nullable
	private String emailquery; // email of the user to search

	@Nullable
	private String nicknamequery; // nickname of the user to search

	@Nullable
	private Role role; // role of the user to search

	@Min(value = 1, message = "Limit must be at least 1")
	@Max(value = 1000, message = "Limit must be at most 1000")
	private int limit = 100; // maximum number of users to return, default is 100
}
