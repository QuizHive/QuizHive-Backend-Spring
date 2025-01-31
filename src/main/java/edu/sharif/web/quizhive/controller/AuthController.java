//package edu.sharif.web.quizhive.controller;
//
//import com.example.demo.service.AuthService;
//import com.example.demo.service.AuthService.LoginResponse;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/auth")
//@Tag(name = "Authentication", description = "Handles user authentication and token management")
//public class AuthController {
//
//	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
//
//	@Autowired
//	private AuthService authService;
//
//	@Operation(summary = "Register a new user", description = "Registers a new user with email, password, nickname, and role.")
//	@ApiResponse(responseCode = "201", description = "User registered successfully")
//	@PostMapping("/register")
//	@ResponseStatus(HttpStatus.CREATED)
//	public RegisterResponse register(@RequestBody RegisterRequest request) {
//		logger.info("Received registration request for email: {}", request.getEmail());
//		User newUser = authService.registerUser(request.getEmail(), request.getPassword(), request.getNickname(), request.getRole());
//		logger.info("Successfully registered user: {}", newUser.getEmail());
//		return new RegisterResponse("User registered successfully", newUser);
//	}
//
//	@Operation(summary = "User login", description = "Authenticates a user and returns access & refresh tokens.")
//	@ApiResponse(responseCode = "200", description = "User logged in successfully")
//	@ApiResponse(responseCode = "401", description = "Invalid credentials")
//	@PostMapping("/login")
//	public LoginResponse login(@RequestBody LoginRequest request) {
//		return authService.loginUser(request.getEmail(), request.getPassword());
//	}
//
//	@Operation(summary = "Refresh JWT token", description = "Generates a new access token using a refresh token.")
//	@ApiResponse(responseCode = "200", description = "Access token refreshed successfully")
//	@ApiResponse(responseCode = "401", description = "Invalid or expired refresh token")
//	@PostMapping("/refreshToken")
//	public ResponseEntity<?> refreshToken(@RequestBody RefreshRequest request) {
//		try {
//			logger.info("Received refresh token request.");
//			String newAccessToken = authService.refreshToken(request.getrToken());
//			logger.info("New access token generated successfully.");
//			return ResponseEntity.ok(new RefreshResponse(newAccessToken));
//		} catch (ExpiredJwtException e) {
//			logger.error("Refresh token expired: {}", e.getMessage());
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Refresh token has expired"));
//		} catch (JwtException e) {
//			logger.error("Invalid refresh token: {}", e.getMessage());
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid refresh token"));
//		} catch (Exception e) {
//			logger.error("Unexpected error: {}", e.getMessage());
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An unexpected error occurred"));
//		}
//	}
//
//	// DTOs
//	public static class RegisterRequest {
//		private String email, password, nickname, role;
//
//		public String getEmail() {return email;}
//
//		public void setEmail(String email) {this.email = email;}
//
//		public String getPassword() {return password;}
//
//		public void setPassword(String password) {this.password = password;}
//
//		public String getNickname() {return nickname;}
//
//		public void setNickname(String nickname) {this.nickname = nickname;}
//
//		public String getRole() {return role;}
//
//		public void setRole(String role) {this.role = role;}
//	}
//
//	public static class RegisterResponse {
//		private String message;
//		private User result;
//
//		public RegisterResponse(String message, User result) {
//			this.message = message;
//			this.result = result;
//		}
//
//		public String getMessage() {return message;}
//
//		public User getResult() {return result;}
//	}
//
//	public static class LoginRequest {
//		private String email, password;
//
//		public String getEmail() {return email;}
//
//		public void setEmail(String email) {this.email = email;}
//
//		public String getPassword() {return password;}
//
//		public void setPassword(String password) {this.password = password;}
//	}
//
//	public static class RefreshRequest {
//		private String rToken;
//
//		public String getrToken() {return rToken;}
//
//		public void setrToken(String rToken) {this.rToken = rToken;}
//	}
//
//	public static class RefreshResponse {
//		private String aToken;
//
//		public RefreshResponse(String aToken) {this.aToken = aToken;}
//
//		public String getaToken() {return aToken;}
//
//		public void setaToken(String aToken) {this.aToken = aToken;}
//	}
//}
