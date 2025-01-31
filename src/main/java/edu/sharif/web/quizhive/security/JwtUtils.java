//package edu.sharif.web.quizhive.security;
//
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.security.Key;
//import java.util.Base64;
//import java.util.Date;
//
//@Component
//public class JwtUtils {
//
//	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
//
//	private final Key secretKey;
//	private final long accessTokenExpiration;
//	private final long refreshTokenExpiration;
//
//	public JwtUtils(
//			@Value("${jwt.secret}") String secret,
//			@Value("${jwt.expiration.access}") long accessTokenExpiration,
//			@Value("${jwt.expiration.refresh}") long refreshTokenExpiration
//	) {
//		byte[] decodedKey = Base64.getDecoder().decode(secret);
//		this.secretKey = Keys.hmacShaKeyFor(decodedKey);
//		this.accessTokenExpiration = accessTokenExpiration;
//		this.refreshTokenExpiration = refreshTokenExpiration;
//	}
//
//	/**
//	 * Generate an Access Token (Short-Lived)
//	 */
//	public String generateAccessToken(String userId) {
//		return generateToken(userId, accessTokenExpiration);
//	}
//
//	/**
//	 * Generate a Refresh Token (Long-Lived)
//	 */
//	public String generateRefreshToken(String userId) {
//		return generateToken(userId, refreshTokenExpiration);
//	}
//
//	/**
//	 * Common method to generate JWT tokens
//	 */
//	private String generateToken(String userId, long expirationTime) {
//		return Jwts.builder()
//				.setSubject(userId)
//				.setIssuedAt(new Date())
//				.setExpiration(new Date(System.currentTimeMillis() + expirationTime))
//				.signWith(secretKey, SignatureAlgorithm.HS256)
//				.compact();
//	}
//
//	/**
//	 * Validate and refresh an Access Token using a Refresh Token
//	 */
//	public String refreshAccessToken(String refreshToken) {
//		logger.info(".................... Received refresh token request: {}", refreshToken);
//		try {
//			Claims claims = parseClaims(refreshToken);
//			logger.info(".................... Refresh token valid for user ID: {}", claims.getSubject());
//
//			if (isTokenExpired(claims)) {
//				logger.warn(".................... Refresh token expired for user ID: {}", claims.getSubject());
//				throw new ExpiredJwtException(null, claims, "Refresh token has expired");
//			}
//
//			return generateAccessToken(claims.getSubject());
//
//		} catch (ExpiredJwtException e) {
//			logger.error(".................... Refresh token expired: {}", e.getMessage());
//			throw new SecurityException("Refresh token has expired", e);
//		} catch (JwtException e) {
//			logger.error(".................... Invalid refresh token: {}", e.getMessage());
//			throw new SecurityException("Invalid refresh token", e);
//		}
//	}
//
//	/**
//	 * Extract User ID from a Token
//	 */
//	public String getUserIdFromToken(String token) {
//		try {
//			Claims claims = parseClaims(token);
//			return claims.getSubject();
//		} catch (JwtException e) {
//			logger.error("Failed to extract user ID from token: {}", e.getMessage());
//			throw new SecurityException("Invalid token", e);
//		}
//	}
//
//	/**
//	 * Parse and Validate Token Claims
//	 */
//	private Claims parseClaims(String token) {
//		return Jwts.parserBuilder()
//				.setSigningKey(secretKey)
//				.build()
//				.parseClaimsJws(token)
//				.getBody();
//	}
//
//	/**
//	 * Check if a Token is Expired
//	 */
//	private boolean isTokenExpired(Claims claims) {
//		return claims.getExpiration().before(new Date());
//	}
//}
