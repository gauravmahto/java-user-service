package com.ecommerce.user_service.component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

  // In production, use a more secure key and store it securely
  // This is just for demonstration purposes
  private static final String SECRET_KEY = "mysecretkeymysecretkeymysecretkey12";

  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    userDetails.getAuthorities().stream().findFirst()
        .ifPresent(authority -> claims.put("role", authority.getAuthority()));
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
        .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
        .compact();
  }

  public Boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  // Validate token using just the token itself clearly
  public Boolean validateToken(String token) {
    try {
      Jwts.parserBuilder()
          // In production, use a more secure key and store it securely
          // This is just for demonstration purposes
          .setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8))
          // .setSigningKey(SECRET_KEY != null ? SECRET_KEY.getBytes() : new byte[0])
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (JwtException e) {
      return false;
    }
  }

  public String extractUsername(String token) {
    return Jwts.parserBuilder()
        // In production, use a more secure key and store it securely
        // This is just for demonstration purposes
        .setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8))
        // .setSigningKey(SECRET_KEY != null ? SECRET_KEY.getBytes() : new byte[0])
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  private Boolean isTokenExpired(String token) {
    return Jwts.parserBuilder()
        // In production, use a more secure key and store it securely
        // This is just for demonstration purposes
        .setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8))
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getExpiration().before(new Date());
  }
}
