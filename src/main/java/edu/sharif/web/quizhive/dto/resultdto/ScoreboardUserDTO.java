package edu.sharif.web.quizhive.dto.resultdto;


import edu.sharif.web.quizhive.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoreboardUserDTO {
	private int rank;
	private String id;
	private String email;
	private String nickname;
	private Role role;
	private Integer score;
}
