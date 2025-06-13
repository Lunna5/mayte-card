package dev.lunna.mayte.service;

import dev.lunna.mayte.config.JWTSettings;
import dev.lunna.mayte.exception.TokenExpiredException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
  private final JWTSettings settings;
  private final SecretKey key;

  public JwtService(@NotNull final JWTSettings settings) {
    this.settings = settings;
    this.key = getSigningKey();
  }

  private SecretKey getSigningKey() {
    byte[] keyBytes = settings.getSecret().getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String generateToken(String email) {
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims, email);
  }

  private String createToken(Map<String, Object> claims, String subject) {
    return Jwts.builder()
        .claims(claims)
        .subject(subject)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + settings.getExpiration()))
        .signWith(getSigningKey())
        .compact();
  }

  public String extractEmail(String token) {
    try {
      return Jwts
          .parser()
          .verifyWith(key)
          .build()
          .parseSignedClaims(token)
          .getPayload()
          .getSubject();
    } catch (JwtException e) {
      throw new TokenExpiredException();
    }
  }

  public boolean isTokenValid(String token, String email) {
    final String userEmailInToken = extractEmail(token);
    return (userEmailInToken.equals(email)) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getExpiration()
        .before(new Date());
  }
}