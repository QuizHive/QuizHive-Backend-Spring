package edu.sharif.web.quizhive.dto.resultdto;

import edu.sharif.web.quizhive.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponse {
	private String message;
	private UserInfoDTO result;
}
