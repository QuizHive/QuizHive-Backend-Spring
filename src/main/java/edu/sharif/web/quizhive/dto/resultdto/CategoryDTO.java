package edu.sharif.web.quizhive.dto.resultdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO {
	private String id;
	private String categoryName;
	private String description;
}
