//package edu.sharif.web.quizhive.security;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.List;
//
//@Component
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//	@Autowired
//	private JwtUtils jwtUtils;
//
//	@Autowired
//	private UserRepository userRepository;
//
//	@Override
//	protected void doFilterInternal(HttpServletRequest request,
//	                                HttpServletResponse response,
//	                                FilterChain filterChain) throws ServletException, IOException {
//		String authHeader = request.getHeader("Authorization");
//
//		String token = null;
//		String userId = null;
//
//		if (authHeader != null && authHeader.startsWith("Bearer ")) {
//			token = authHeader.substring(7); // Remove "Bearer " prefix
//			try {
//				userId = jwtUtils.getUserIdFromToken(token);
//			} catch (JwtException e) {
//				logger.error("Invalid JWT token: {}");
//				throw new RuntimeException("Invalid JWT token"); // Correctly throw an Exception
//			}
//		}
//
//		if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//			User user = userRepository.findById(userId).orElse(null);
//			if (user != null) {
//				List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
//
//				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//						user, null, authorities
//				);
//				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//				SecurityContextHolder.getContext().setAuthentication(authentication);
//			}
//		}
//		filterChain.doFilter(request, response);
//	}
//}
