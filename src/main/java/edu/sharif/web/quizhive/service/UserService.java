package edu.sharif.web.quizhive.service;

import edu.sharif.web.quizhive.dto.resultdto.ScoreboardUserDTO;
import edu.sharif.web.quizhive.dto.resultdto.UserInfoDTO;
import edu.sharif.web.quizhive.exception.NotFoundException;
import edu.sharif.web.quizhive.model.User;
import edu.sharif.web.quizhive.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	// Use LevenshteinDistance to find the closest objects to the query
	private static <T> List<T> search(List<T> list, Function<T, String> stringFunction, String query, int limit) {
		PriorityQueue<ObjSimilarity<T>> top = new PriorityQueue<>(limit, Comparator.comparingDouble(us -> -us.similarity));
		list.stream().forEach(element -> {
			double similarity = calculateSimilarityPercentage(query, stringFunction.apply(element));
			if (similarity >= 50.0) {  // Only consider users with at least 50% similarity
				if (top.size() < limit) {
					top.add(new ObjSimilarity<>(element, similarity));
				} else if (top.peek().similarity < similarity) {
					top.poll();
					top.add(new ObjSimilarity<>(element, similarity));
				}
			}
		});
		return top.stream()
				.sorted(Comparator.comparingDouble(us -> -us.similarity))
				.map(us -> us.obj)
				.collect(Collectors.toList());
	}

	private static double calculateSimilarityPercentage(String s1, String s2) {
		int distance = levenshteinDistance(s1, s2);
		int maxLen = Math.max(s1.length(), s2.length());
		return maxLen == 0 ? 100 : (1 - (double) distance / maxLen) * 100;
	}

	private static int levenshteinDistance(String s1, String s2) {
		int len1 = s1.length();
		int len2 = s2.length();

		int[][] dp = new int[len1 + 1][len2 + 1];

		for (int i = 0; i <= len1; i++) {
			for (int j = 0; j <= len2; j++) {
				if (i == 0) {
					dp[i][j] = j;
				} else if (j == 0) {
					dp[i][j] = i;
				} else if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
					dp[i][j] = dp[i - 1][j - 1];
				} else {
					dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
				}
			}
		}

		return dp[len1][len2];
	}

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

	public List<UserInfoDTO> searchUsers(String query, int limit) {
		return search(userRepository.findAll(), User::getNickname, query, limit)
				.stream()
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

	private record ObjSimilarity<T>(T obj, double similarity) {}
}
