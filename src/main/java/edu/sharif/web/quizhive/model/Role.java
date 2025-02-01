package edu.sharif.web.quizhive.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
	PLAYER,
	ADMIN;

	@JsonCreator
	public static Role fromString(String key) {
		return key == null ? null : Role.valueOf(key.toUpperCase());
	}

	@JsonValue
	public String toString() {
		return name().toLowerCase();
	}
}
