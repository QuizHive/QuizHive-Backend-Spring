package edu.sharif.web.quizhive.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtils {

	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	private final Key secretKey;
	private final long accessTokenExpiration;
	private final long refreshTokenExpiration;

	public JwtUtils(
			@Value("${jwt.secret}") String secret,
			@Value("${jwt.expiration.access}") long accessTokenExpiration,
			@Value("${jwt.expiration.refresh}") long refreshTokenExpiration
	) {
		byte[] decodedKey = Base64.getDecoder().decode(secret);
		this.secretKey = Keys.hmacShaKeyFor(decodedKey);
		this.accessTokenExpiration = accessTokenExpiration;
		this.refreshTokenExpiration = refreshTokenExpiration;
	}

	public String generateAccessToken(String userId) {
		return generateToken(userId, accessTokenExpiration);
	}

	public String generateRefreshToken(String userId) {
		return generateToken(userId, refreshTokenExpiration);
	}

	public String generateToken(String sub, long expirationTime) {
		return Jwts.builder()
				.setSubject(sub)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expirationTime))
				.setId(UUID.randomUUID().toString())
				.claim("access", expirationTime == accessTokenExpiration)
				.signWith(secretKey, SignatureAlgorithm.HS256)
				.compact();
	}

	private Claims parseClaims(String token) {
		return Jwts.parser()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	private boolean isTokenExpired(Claims claims) {
		return claims.getExpiration().before(new Date());
	}

	// New method to extract and validate the token
	public String getUsernameFromRequest(HttpServletRequest request) {
		try {
			// Get the Authorization header
			String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

			// Check if the header has the Bearer token
			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
				if (validateToken(token)) {
					Claims claims = parseClaims(token);
					// Check if it's an access token
					if(claims.get("access", Boolean.class))
						return claims.getSubject();
				}
			}
		} catch (JwtException e) {
			logger.error("Invalid token: {}", e.getMessage());
		}
		return null; // Or throw an exception as needed
	}

	// Validate the token
	public boolean validateToken(String token) {
		try {
			parseClaims(token);
			return true; // Token is valid
		} catch (Exception e) {
			return false; // Token is invalid
		}
	}

	public String refreshAccessToken(String refreshToken) {
		logger.info(".................... Received refresh token request: {}", refreshToken);
		try {
			Claims claims = parseClaims(refreshToken);
			logger.info(".................... Refresh token valid for user ID: {}", claims.getSubject());

			if (isTokenExpired(claims)) {
				logger.warn(".................... Refresh token expired for user ID: {}", claims.getSubject());
				throw new ExpiredJwtException(null, claims, "Refresh token has expired");
			}

			return generateAccessToken(claims.getSubject());
		} catch (ExpiredJwtException e) {
			logger.error(".................... Refresh token expired: {}", e.getMessage());
			throw new SecurityException("Refresh token has expired", e);
		} catch (JwtException e) {
			logger.error(".................... Invalid refresh token: {}", e.getMessage());
			throw new SecurityException("Invalid refresh token", e);
		}
	}
}
