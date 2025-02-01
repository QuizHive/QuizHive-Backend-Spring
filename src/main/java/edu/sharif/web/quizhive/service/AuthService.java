package edu.sharif.web.quizhive.service;

import edu.sharif.web.quizhive.dto.resultdto.TokenDTO;
import edu.sharif.web.quizhive.dto.resultdto.UserInfoDTO;
import edu.sharif.web.quizhive.exception.ConflictException;
import edu.sharif.web.quizhive.exception.UnauthorizedException;
import edu.sharif.web.quizhive.model.Role;
import edu.sharif.web.quizhive.model.User;
import edu.sharif.web.quizhive.repository.UserRepository;
import edu.sharif.web.quizhive.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {
	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
	private final UserRepository userRepository;
	private final JwtUtils jwtUtils;
	private final PasswordEncoder passwordEncoder;

	public UserInfoDTO registerUser(String email, String password, String nickname, Role role) {
		logger.info("Attempting to register user with email: {}", email);
		if (userRepository.existsByEmail(email)) {
			logger.warn("Registration failed: User already exists with email: {}", email);
			throw new ConflictException("User already exists");
		}

		if (password == null || password.isEmpty()) {
			throw new IllegalArgumentException("Password cannot be null or empty");
		}

		String hashedPassword = passwordEncoder.encode(password);

		User newUser = User.builder()
				.email(email)
				.passHash(hashedPassword)
				.nickname(nickname)
				.role(role)
				.score(0)
				.createdAt(new Date())
				.build();
		newUser = userRepository.save(newUser);

		logger.info("User registered successfully: {}", email);

		return UserInfoDTO.builder()
				.id(newUser.getId())
				.email(newUser.getEmail())
				.nickname(newUser.getNickname())
				.role(newUser.getRole())
				.score(newUser.getScore())
				.createdAt(newUser.getCreatedAt())
				.build();
	}

	public TokenDTO loginUser(String email, String rawPassword) {
		logger.info("Login attempt for email: {}", email);

		User user = userRepository.findByEmail(email);
		if (user == null) {
			logger.warn("Login FAILED: User not found for email: {}", email);
			throw new UnauthorizedException("Invalid credentials");
		}

		logger.info("User found: {}", user.getEmail());
		logger.info("Checking password match...");

		if (!passwordEncoder.matches(rawPassword, user.getPassHash())) {
			logger.warn("Login FAILED: Password mismatch for email: {}", email);
			throw new UnauthorizedException("Invalid credentials");
		}

		logger.info("Password MATCHED. Generating JWT tokens...");
		String rToken = jwtUtils.generateRefreshToken(user.getId());
		String aToken = jwtUtils.generateAccessToken(user.getId());

		logger.info("Login successful for email: {}", email);
		return TokenDTO.builder()
				.accessToken(aToken)
				.refreshToken(rToken)
				.build();
	}

	public TokenDTO refreshToken(String t) {
		logger.info("Received refresh token: {}", t);
		System.out.printf("Received refresh token: %s\n", t);
		var aToken = jwtUtils.refreshAccessToken(t);
		logger.info("Token refreshed successfully");
		return TokenDTO.builder().accessToken(aToken).refreshToken(t).build();
	}
}
