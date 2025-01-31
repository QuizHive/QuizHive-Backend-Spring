package edu.sharif.web.quizhive.model;

import lombok.Getter;

@Getter
public enum Difficulty {
	EASY(1),
	MEDIUM(2),
	HARD(3);

	private final int level;

	Difficulty(int level) {
		this.level = level;
	}
}