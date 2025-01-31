//package edu.sharif.web.quizhive.service;
//
//import edu.sharif.web.quizhive.exception.ConflictException;
//import edu.sharif.web.quizhive.model.Role;
//import edu.sharif.web.quizhive.model.User;
//import edu.sharif.web.quizhive.repository.UserRepository;
//import edu.sharif.web.quizhive.security.JwtUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//
//@Service
//public class AuthService {
//
//	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
//
//	@Autowired
//	private UserRepository userRepository;
//
//	@Autowired
//	private JwtUtils jwtUtils;
//
//	@Autowired
//	private PasswordEncoder passwordEncoder;
//
//	/**
//	 * Register a new user
//	 */
//	public User registerUser(String email, String password, String nickname, String roleStr) {
//		logger.info("Attempting to register user with email: {}", email);
//		if (userRepository.existsByEmail(email)) {
//			logger.warn("Registration failed: User already exists with email: {}", email);
//			throw new ConflictException("User already exists");
//		}
//
//		Role role;
//		try {
//			role = Role.valueOf(roleStr.toUpperCase());
//		} catch (IllegalArgumentException e) {
//			logger.error("Invalid role provided: {}", roleStr);
//			throw new IllegalArgumentException("Invalid role. Allowed values: player, admin.");
//		}
//
//		if (password == null || password.isEmpty()) {
//			throw new IllegalArgumentException("Password cannot be null or empty");
//		}
//
//		String hashedPassword = passwordEncoder.encode(password);
//
//		User newUser = new User(email, hashedPassword, nickname, role, 0, new Date();
//		userRepository.save(newUser);
//		logger.info("User registered successfully: {}", email);
//		return newUser;
//	}
//
//	/**
//	 * Login a user
//	 */
//	public LoginResponse loginUser(String email, String rawPassword) {
//		logger.info("............................. Login attempt for email: {}", email);
//
//		User user = userRepository.findByEmail(email);
//		if (user == null) {
//			logger.warn("-------------------------------- Login failed: User not found for email: {}", email);
//			throw new UnauthorizedException("Invalid credentials");
//		}
//
//		logger.info("+++++++++++++++++++++++++++++++++ User found: {}", user.getEmail());
//		logger.info("+++++++++++++++++++++++++++++++++ Checking password match...");
//
//		if (!passwordEncoder.matches(rawPassword, user.getPassHash())) {
//			logger.warn("-------------------------------- Login failed: Password mismatch for email: {}", email);
//			throw new UnauthorizedException("Invalid credentials");
//		}
//
//		logger.info("+++++++++++++++++++++++++++++++++ Password matched. Generating JWT tokens...");
//		String rToken = jwtUtils.generateRefreshToken(user.getId());
//		String aToken = jwtUtils.generateAccessToken(user.getId());
//
//		logger.info("+++++++++++++++++++++++++++++++++ Login successful for email: {}", email);
//		return new LoginResponse(rToken, aToken);
//	}
//
//	/**
//	 * Refresh the access token
//	 */
//	public String refreshToken(String rToken) {
//		logger.info("Refreshing token...");
//		return jwtUtils.refreshAccessToken(rToken);
//	}
//
//	public static class LoginResponse {
//		private String rToken;
//		private String aToken;
//
//		public LoginResponse(String rToken, String aToken) {
//			this.rToken = rToken;
//			this.aToken = aToken;
//		}
//
//		public String getrToken() {
//			return rToken;
//		}
//
//		public void setrToken(String rToken) {
//			this.rToken = rToken;
//		}
//
//		public String getaToken() {
//			return aToken;
//		}
//
//		public void setaToken(String aToken) {
//			this.aToken = aToken;
//		}
//	}
//}
