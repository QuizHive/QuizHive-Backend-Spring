package edu.sharif.web.quizhive.security;

import edu.sharif.web.quizhive.model.LoggedInUser;
import edu.sharif.web.quizhive.model.User;
import edu.sharif.web.quizhive.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(JwtAuthenticationFilter.class);
	private final JwtUtils jwtUtils;
	private final UserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
	                                HttpServletResponse response,
	                                FilterChain filterChain) throws ServletException, IOException {
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		String userId = null;
		logger.info("Auth Header: {}", authHeader);
		userId = jwtUtils.getUsernameFromRequest(request);

		if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			logger.info("Authenticating user with id: {}", userId);
			User user = userRepository.findById(userId).orElse(null);
			if (user != null) {
				logger.info("User authenticated: {}", user.getEmail());
				LoggedInUser loggedInUser = new LoggedInUser(user, user.getId(), user.getId(),
						List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
				logger.info("Authorities: {}", loggedInUser.getAuthorities());
				Authentication authentication = new UsernamePasswordAuthenticationToken(loggedInUser, null, loggedInUser.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		filterChain.doFilter(request, response);
	}
}
