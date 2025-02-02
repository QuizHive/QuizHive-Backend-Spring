package edu.sharif.web.quizhive.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.sharif.web.quizhive.dto.resultdto.TokenDTO;
import edu.sharif.web.quizhive.dto.resultdto.UserInfoDTO;
import edu.sharif.web.quizhive.model.Role;
import edu.sharif.web.quizhive.model.User;
import edu.sharif.web.quizhive.repository.UserRepository;
import edu.sharif.web.quizhive.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoogleAuthService {
	private final UserRepository userRepository;
	private final AuthService authService;
	private final UserService userService;
	private final JwtUtils jwtUtils;

	@Value("${google.client.id}")
	private String clientId;

	@Value("${google.client.secret}")
	private String clientSecret;

	@Value("${google.redirect.uri}")
	private String redirect_uri;

	public TokenDTO processGrantCode(String code) {
		String accessToken = getOauthAccessTokenGoogle(code);
		UserInfoDTO googleUser = getProfileDetailsGoogle(accessToken);
		User user = userRepository.findByEmail(googleUser.getEmail());
		if (user == null)
			authService.registerUser(googleUser.getEmail(), UUID.randomUUID().toString(),
					googleUser.getNickname(), googleUser.getRole());

		user = userRepository.findByEmail(googleUser.getEmail());
		return TokenDTO.builder()
				.refreshToken(jwtUtils.generateRefreshToken(user.getId()))
				.accessToken(jwtUtils.generateAccessToken(user.getId()))
				.build();
	}

	private UserInfoDTO getProfileDetailsGoogle(String accessToken) {
		var requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setConnectTimeout(20000);
		requestFactory.setConnectionRequestTimeout(20000);
		requestFactory.setReadTimeout(30000);
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setBearerAuth(accessToken);
		HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);
		String url = "https://www.googleapis.com/oauth2/v2/userinfo";
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			JsonNode jsonObject = objectMapper.readTree(response.getBody());
			return UserInfoDTO.builder()
					.email(jsonObject.get("email").asText())
					.nickname(jsonObject.get("name").asText())
					.role(Role.PLAYER)
					.build();
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	private String getOauthAccessTokenGoogle(String code) {
		var requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setConnectTimeout(20000);
		requestFactory.setConnectionRequestTimeout(20000);
		requestFactory.setReadTimeout(30000);
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("code", code);
		params.add("redirect_uri", redirect_uri);
		params.add("client_id", clientId);
		params.add("client_secret", clientSecret);
		params.add("scope", "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile");
		params.add("scope", "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email");
		params.add("scope", "openid");
		params.add("grant_type", "authorization_code");

		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, httpHeaders);

		String url = "https://oauth2.googleapis.com/token";
		String response = restTemplate.postForObject(url, requestEntity, String.class);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			JsonNode jsonObject = objectMapper.readTree(response);
			return jsonObject.get("access_token").toString().replace("\"", "");
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
