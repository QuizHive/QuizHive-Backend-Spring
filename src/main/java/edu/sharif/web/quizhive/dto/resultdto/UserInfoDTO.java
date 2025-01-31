package edu.sharif.web.quizhive.dto.resultdto;

import edu.sharif.web.quizhive.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoDTO {
	private String id;
	private String email;
	private String nickname;
	private Role role;
	private Integer score;
	private Date createdAt;
}