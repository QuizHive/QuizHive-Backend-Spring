package edu.sharif.web.quizhive.controller;

import edu.sharif.web.quizhive.dto.resultdto.UserInfoDTO;
import edu.sharif.web.quizhive.model.LoggedInUser;
import edu.sharif.web.quizhive.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User-related operations, including profile retrieval and leaderboard")
public class UserController {
	private final UserService userService;

	@Operation(summary = "Get logged-in user's info", description = "Returns the currently logged-in user's details.")
	@ApiResponse(responseCode = "200", description = "User information retrieved successfully")
	@GetMapping("/me")
	public UserInfoDTO getCurrentUserInfo(Principal principal) {
		String userId = principal.getName();
		return userService.getUserInfo(userId);
	}

	@Operation(summary = "Get user info by ID", description = "Fetches a user's information using their ID.")
	@ApiResponse(responseCode = "200", description = "User found")
	@ApiResponse(responseCode = "404", description = "User not found")
	@GetMapping("/info/{userId}")
	public UserInfoDTO getUserById(@PathVariable("userId") String userId) {
		return userService.getUserInfo(userId);
	}

	@Operation(summary = "Get leaderboard", description = "Returns the top users sorted by score.")
	@ApiResponse(responseCode = "200", description = "Leaderboard data retrieved successfully")
	@GetMapping("/leaderboard")
	@PreAuthorize("hasRole('PLAYER')")
	public Map<String, Object> getScoreboard(@RequestParam(name = "limit", defaultValue = "10") int limit,
	                                         @AuthenticationPrincipal LoggedInUser user) {
		String userId = user.get().getId();
		return userService.getScoreboard(userId, limit);
	}
}
