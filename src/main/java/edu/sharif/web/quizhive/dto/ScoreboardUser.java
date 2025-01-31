package edu.sharif.web.quizhive.dto;


import edu.sharif.web.quizhive.model.Role;

public class ScoreboardUser {
	private int rank;
	private String id;
	private String email;
	private String nickname;
	private Role role;
	private Integer score;

	public ScoreboardUser() {}

	public ScoreboardUser(int rank, String id, String email, String nickname, Role role, Integer score) {
		this.rank = rank;
		this.id = id;
		this.email = email;
		this.nickname = nickname;
		this.role = role;
		this.score = score;
	}

	// Getters / Setters
	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
}