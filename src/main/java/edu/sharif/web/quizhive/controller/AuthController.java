package edu.sharif.web.quizhive.controller;

import edu.sharif.web.quizhive.dto.requestdto.LoginReqDTO;
import edu.sharif.web.quizhive.dto.requestdto.RegisterReqDTO;
import edu.sharif.web.quizhive.dto.resultdto.RegisterResponse;
import edu.sharif.web.quizhive.dto.resultdto.TokenDTO;
import edu.sharif.web.quizhive.dto.resultdto.UserInfoDTO;
import edu.sharif.web.quizhive.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Handles user authentication and token management")
public class AuthController {

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	private final AuthService authService;

	@Operation(summary = "Register a new user", description = "Registers a new user with email, password, nickname, and role.")
	@ApiResponse(responseCode = "201", description = "User registered successfully")
	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public RegisterResponse register(@RequestBody RegisterReqDTO request) {
		logger.info("Received registration request for email: {}", request.getEmail());
		UserInfoDTO newUser = authService.registerUser(request.getEmail(), request.getPassword(), request.getNickname(), request.getRole());
		logger.info("Successfully registered user: {}", newUser.getEmail());
		return new RegisterResponse("User registered successfully", newUser);
	}

	@Operation(summary = "User login", description = "Authenticates a user and returns access & refresh tokens.")
	@ApiResponse(responseCode = "200", description = "User logged in successfully")
	@ApiResponse(responseCode = "401", description = "Invalid credentials")
	@PostMapping("/login")
	public TokenDTO login(@RequestBody LoginReqDTO request) {
		return authService.loginUser(request.getEmail(), request.getPassword());
	}

	@Operation(summary = "Refresh JWT token", description = "Generates a new access token using a refresh token.")
	@ApiResponse(responseCode = "200", description = "Access token refreshed successfully")
	@ApiResponse(responseCode = "401", description = "Invalid or expired refresh token")
	@PostMapping("/refresh-token")
	@ResponseStatus(HttpStatus.OK)
	public TokenDTO refreshToken(@RequestBody TokenDTO dto) {
		logger.info("Received refresh token request.");
		System.out.println(dto.getRefreshToken());
		String rToken = dto.getRefreshToken();
		TokenDTO result = authService.refreshToken(rToken);
		logger.info("New access token generated successfully.");
		return result;
	}
}
