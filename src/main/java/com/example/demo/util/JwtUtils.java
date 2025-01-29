package com.example.demo.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

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
        // Ensure the secret key is valid for HS256 algorithm
        if (secret == null || secret.length() < 32) {
            throw new IllegalArgumentException("JWT secret key must be at least 32 characters long");
        }
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    /**
     * Generate an Access Token (Short-Lived)
     */
    public String generateAccessToken(String userId) {
        return generateToken(userId, accessTokenExpiration);
    }

    /**
     * Generate a Refresh Token (Long-Lived)
     */
    public String generateRefreshToken(String userId) {
        return generateToken(userId, refreshTokenExpiration);
    }

    /**
     * Common method to generate JWT tokens
     */
    private String generateToken(String userId, long expirationTime) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validate and refresh an Access Token using a Refresh Token
     */
    public String refreshAccessToken(String refreshToken) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();

            // Check if token is expired
            if (claims.getExpiration().before(new Date())) {
                logger.warn("Refresh token expired for userId: {}", claims.getSubject());
                throw new ExpiredJwtException(null, claims, "Refresh token has expired");
            }

            // Generate new access token
            return generateAccessToken(claims.getSubject());

        } catch (ExpiredJwtException e) {
            logger.error("Refresh token expired: {}", e.getMessage());
            throw e;
        } catch (JwtException e) {
            logger.error("Invalid refresh token: {}", e.getMessage());
            throw new RuntimeException("Invalid refresh token");
        } catch (Exception e) {
            logger.error("Unexpected error while refreshing token: {}", e.getMessage());
            throw new RuntimeException("Internal server error");
        }
    }

    /**
     * Extract User ID from a Token
     */
    public String getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (JwtException e) {
            logger.error("Failed to extract user ID from token: {}", e.getMessage());
            throw new RuntimeException("Invalid token");
        }
    }

    /**
     * Check if a Token is Expired
     */
    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
}
