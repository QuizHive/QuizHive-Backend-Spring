package edu.sharif.web.quizhive.service;

import edu.sharif.web.quizhive.dto.resultdto.ScoreboardUserDTO;
import edu.sharif.web.quizhive.dto.resultdto.UserInfoDTO;
import edu.sharif.web.quizhive.exception.NotFoundException;
import edu.sharif.web.quizhive.model.User;
import edu.sharif.web.quizhive.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	public UserInfoDTO getUserInfo(String id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("User not found"));
		return convertToUserInfo(user);
	}

	public List<UserInfoDTO> getAllUsers() {
		List<User> users = userRepository.findAll();
		return users.stream()
				.map(this::convertToUserInfo)
				.collect(Collectors.toList());
	}

	/**
	 * Retrieves the top n users sorted by score.
	 * If the given user is not in the top n, append them with their actual rank.
	 *
	 * @param userId ID of the "logged-in" user
	 * @param n      number of top users to retrieve
	 * @return A map containing:
	 * - scoreboard: List of top n (or n+1 if logged in user is outside top n)
	 * - userRank: The scoreboard data for the logged-in user
	 */
	public Map<String, Object> getScoreboard(String userId, int n) {
		List<User> allUsers = userRepository.findAll();
		allUsers.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
		List<User> topUsers = allUsers.stream().limit(n).toList();
		List<ScoreboardUserDTO> scoreboard = new ArrayList<>();
		for (int i = 0; i < topUsers.size(); i++) {
			User u = topUsers.get(i);
			ScoreboardUserDTO su = new ScoreboardUserDTO(
					i + 1,         // rank (1-based)
					u.getId(),
					u.getEmail(),
					u.getNickname(),
					u.getRole(),
					u.getScore()
			);
			scoreboard.add(su);
		}

		User loggedInUser = userRepository.findById(userId)
				.orElseThrow(() -> new NotFoundException("User not found"));

		ScoreboardUserDTO userRank = null;

		Optional<ScoreboardUserDTO> inTop = scoreboard.stream()
				.filter(su -> su.getId().equals(loggedInUser.getId()))
				.findFirst();

		if (inTop.isEmpty()) {
			int rank = 1; // rank is 1-based
			for (User u : allUsers) {
				if (u.getScore() > loggedInUser.getScore()) {
					rank++;
				}
			}
			userRank = new ScoreboardUserDTO(rank,
					loggedInUser.getId(),
					loggedInUser.getEmail(),
					loggedInUser.getNickname(),
					loggedInUser.getRole(),
					loggedInUser.getScore());

			scoreboard.add(userRank);
		} else {
			userRank = inTop.get();
		}
		Map<String, Object> result = new HashMap<>();
		result.put("scoreboard", scoreboard);
		result.put("userRank", userRank);
		return result;
	}

	/**
	 * Helper method: Convert User to UserInfoDTO
	 */
	private UserInfoDTO convertToUserInfo(User user) {
		return new UserInfoDTO(
				user.getId(),
				user.getEmail(),
				user.getNickname(),
				user.getRole(),
				user.getScore(),
				user.getCreatedAt()
		);
	}
}
