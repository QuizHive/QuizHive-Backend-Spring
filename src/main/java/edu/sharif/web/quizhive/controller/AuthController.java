package edu.sharif.web.quizhive.controller;


import edu.sharif.web.quizhive.model.User;
import edu.sharif.web.quizhive.service.AuthService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private AuthService authService;

	/**
	 * POST /auth/register
	 */
	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public RegisterResponse register(@RequestBody RegisterRequest request) {
		logger.info("Received registration request for email: {}", request.getEmail());
		User newUser = authService.registerUser(
				request.getEmail(),
				request.getPassword(), // Pass raw password
				request.getNickname(),
				request.getRole()
		);
		logger.info("Successfully registered user: {}", newUser.getEmail());
		return new RegisterResponse("User registered successfully", newUser);
	}

	/**
	 * POST /auth/login
	 */
	@PostMapping("/login")
	public AuthService.LoginResponse login(@RequestBody LoginRequest request) {
		return authService.loginUser(request.getEmail(), request.getPassword());
	}

	/**
	 * POST /auth/refreshToken
	 */
	@PostMapping("/refreshToken")
	public ResponseEntity<?> refreshToken(@RequestBody RefreshRequest request) {
		try {
			logger.info("Received refresh token request.");
			String newAccessToken = authService.refreshToken(request.getrToken());
			logger.info("********** New access token generated successfully.");
			return ResponseEntity.ok(new RefreshResponse(newAccessToken));
		} catch (ExpiredJwtException e) {
			logger.error("Refresh token expired: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("error", "Refresh token has expired"));
		} catch (JwtException e) {
			logger.error("Invalid refresh token: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("error", "Invalid refresh token"));
		} catch (Exception e) {
			logger.error("Unexpected error: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "An unexpected error occurred"));
		}
	}

	// --- Request / Response DTOs ---

	// /auth/register request
	public static class RegisterRequest {
		private String email;
		private String password;
		private String nickname;
		private String role;

		// Getters / Setters
		public String getEmail() {return email;}

		public void setEmail(String email) {this.email = email;}

		public String getPassword() {return password;}

		public void setPassword(String password) {this.password = password;}

		public String getNickname() {return nickname;}

		public void setNickname(String nickname) {this.nickname = nickname;}

		public String getRole() {return role;}

		public void setRole(String role) {this.role = role;}
	}

	public static class RegisterResponse {
		private String message;
		private User result;

		public RegisterResponse(String message, User result) {
			this.message = message;
			this.result = result;
		}

		public String getMessage() {
			return message;
		}

		public User getResult() {
			return result;
		}
	}

	// /auth/login request
	public static class LoginRequest {
		private String email;
		private String password;

		public String getEmail() {return email;}

		public void setEmail(String email) {this.email = email;}

		public String getPassword() {return password;}

		public void setPassword(String password) {this.password = password;}
	}

	// /auth/refreshToken request
	public static class RefreshRequest {
		private String rToken;

		public String getrToken() {return rToken;}

		public void setrToken(String rToken) {this.rToken = rToken;}
	}

	public static class RefreshResponse {
		private String aToken;

		public RefreshResponse(String aToken) {
			this.aToken = aToken;
		}

		public String getaToken() {return aToken;}

		public void setaToken(String aToken) {this.aToken = aToken;}
	}
}
