package edu.sharif.web.quizhive.dto.requestdto;

import edu.sharif.web.quizhive.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterReqDTO {
	@Email(message = "Email should be valid")
	@NotBlank(message = "Email is mandatory")
	private String email;

	@NotBlank(message = "Password is mandatory")
	@Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
	private String password;

	@NotBlank(message = "Nickname is mandatory")
	@Size(min = 3, max = 15, message = "Nickname must be between 3 and 15 characters")
	private String nickname;

	@NotNull(message = "Role is mandatory")
	private Role role;
}
