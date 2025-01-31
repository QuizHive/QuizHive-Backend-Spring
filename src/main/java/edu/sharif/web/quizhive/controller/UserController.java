package edu.sharif.web.quizhive.controller;

import edu.sharif.web.quizhive.dto.UserInfo;
import edu.sharif.web.quizhive.exception.NotFoundException;
import edu.sharif.web.quizhive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * GET /api/users/me
	 * Returns the currently logged-in user's info.
	 */
	@GetMapping("/me")
	public UserInfo getCurrentUserInfo(Principal principal) {
		// Example: principal.getName() returns the userId
		String userId = principal.getName();
		return userService.getUserInfo(userId);
	}

	/**
	 * GET /api/users/info/{userId}
	 * Returns a user's info by ID.
	 */
	@GetMapping("/info/{userId}")
	public UserInfo getUserById(@PathVariable("userId") String userId) {
		return userService.getUserInfo(userId);
	}

	/**
	 * GET /api/users/leaderboard?limit=10
	 * Returns scoreboard data.
	 */
	@GetMapping("/leaderboard")
	public Map<String, Object> getScoreboard(
			@RequestParam(name = "limit", defaultValue = "10") int limit,
			Principal principal
	) {
		String userId = principal.getName();
		return userService.getScoreboard(userId, limit);
	}

	@ExceptionHandler(NotFoundException.class)
	public Map<String, String> handleNotFound(NotFoundException ex) {
		return Map.of("error", ex.getMessage());
	}
}