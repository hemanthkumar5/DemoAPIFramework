package com.hemanth.core;

import com.hemanth.config.ConfigManager;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT Token Manager for authentication, token expiration, and renewal
 */
public class TokenManager {
    
    private static TokenManager instance;
    private final SecretKey secretKey;
    private final long expirationTime;
    private String currentToken;
    private Date tokenExpiration;
    
    // Private constructor
    private TokenManager() {
        ConfigManager config = ConfigManager.getInstance();
        this.secretKey = Keys.hmacShaKeyFor(config.getJwtSecret().getBytes());
        this.expirationTime = config.getJwtExpiration();
    }
    
    /**
     * Get singleton instance
     */
    public static synchronized TokenManager getInstance() {
        if (instance == null) {
            instance = new TokenManager();
        }
        return instance;
    }
    
    /**
     * Generate JWT token for a user
     */
    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("role", role);
        
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTime);
        
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
        
        this.currentToken = token;
        this.tokenExpiration = expiration;
        
        return token;
    }
    
    /**
     * Generate token with custom claims
     */
    public String generateToken(Map<String, Object> claims) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTime);
        
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
        
        this.currentToken = token;
        this.tokenExpiration = expiration;
        
        return token;
    }
    
    /**
     * Validate JWT token
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    
    /**
     * Extract claims from token
     */
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
    /**
     * Extract username from token
     */
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }
    
    /**
     * Extract role from token
     */
    public String extractRole(String token) {
        Claims claims = extractClaims(token);
        return claims.get("role", String.class);
    }
    
    /**
     * Check if token is expired
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = extractClaims(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
    
    /**
     * Check if current token is expired
     */
    public boolean isCurrentTokenExpired() {
        return currentToken == null || isTokenExpired(currentToken);
    }
    
    /**
     * Get time until token expires (in milliseconds)
     */
    public long getTimeUntilExpiration() {
        if (tokenExpiration == null) {
            return 0;
        }
        return tokenExpiration.getTime() - System.currentTimeMillis();
    }
    
    /**
     * Check if token needs renewal (expires within 5 minutes)
     */
    public boolean needsRenewal() {
        long timeUntilExpiration = getTimeUntilExpiration();
        return timeUntilExpiration > 0 && timeUntilExpiration < 300000; // 5 minutes
    }
    
    /**
     * Renew current token
     */
    public String renewToken() {
        if (currentToken == null) {
            throw new IllegalStateException("No current token to renew");
        }
        
        try {
            Claims claims = extractClaims(currentToken);
            String username = claims.getSubject();
            String role = claims.get("role", String.class);
            
            return generateToken(username, role);
        } catch (Exception e) {
            throw new RuntimeException("Failed to renew token", e);
        }
    }
    
    /**
     * Get current token
     */
    public String getCurrentToken() {
        return currentToken;
    }
    
    /**
     * Set current token
     */
    public void setCurrentToken(String token) {
        this.currentToken = token;
        try {
            Claims claims = extractClaims(token);
            this.tokenExpiration = claims.getExpiration();
        } catch (Exception e) {
            this.tokenExpiration = null;
        }
    }
    
    /**
     * Clear current token
     */
    public void clearToken() {
        this.currentToken = null;
        this.tokenExpiration = null;
    }
    
    /**
     * Get authorization header value
     */
    public String getAuthorizationHeader() {
        if (currentToken == null) {
            throw new IllegalStateException("No token available");
        }
        return "Bearer " + currentToken;
    }
    
    /**
     * Check if token is valid and not expired
     */
    public boolean isTokenValid(String token) {
        return token != null && validateToken(token) && !isTokenExpired(token);
    }
}
