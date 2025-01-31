package edu.sharif.web.quizhive.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "users")
public class User {

	@Id
	private String id;

	private String email;
	private String passHash;
	private String nickname;
	private Role role;
	private int score;
	private Date createdAt;

	public User() {}

	public User(String email, String passHash, String nickname, Role role, int score, Date createdAt) {
		this.email = email;
		this.passHash = passHash;
		this.nickname = nickname;
		this.role = role;
		this.score = score;
		this.createdAt = createdAt;
	}

	// Getters / Setters
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

	public String getPassHash() {
		return passHash;
	}

	public void setPassHash(String passHash) {
		this.passHash = passHash;
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

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}