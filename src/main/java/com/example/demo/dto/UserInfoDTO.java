package com.example.demo.dto;

import com.example.demo.model.Role;

import java.util.Date;

public class UserInfoDTO {
    private String id;
    private String email;
    private String nickname;
    private Role role;
    private Integer score;
    private Date createdAt;

    public UserInfoDTO() {}

    public UserInfoDTO(String id, String email, String nickname, Role role, Integer score, Date createdAt) {
        this.id = id;
        this.email = email;
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

    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}