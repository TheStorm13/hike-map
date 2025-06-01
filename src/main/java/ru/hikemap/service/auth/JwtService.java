package ru.hikemap.service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtService {

  // Access: 30 минут, Refresh: 7 дней
  private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 30;
  private static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7;

  private final String secret;

  public JwtService(@Value("${jwt.secret}") String secret) {
    this.secret = secret;
  }

  public String generateAccessToken(String email) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("tokenType", "accessToken");
    return createToken(claims, email, ACCESS_TOKEN_EXPIRATION);
  }

  public String generateRefreshToken(String email) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("tokenType", "refreshToken");
    return createToken(claims, email, REFRESH_TOKEN_EXPIRATION);
  }

  private String createToken(
    Map<String, Object> claims,
    String email,
    long expiration
  ) {
    return Jwts.builder()
      .setClaims(claims)
      .setSubject(email)
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + expiration))
      .signWith(getSignKey(), SignatureAlgorithm.HS256)
      .compact();
  }

  private Key getSignKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secret);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public String extractTokenType(String token) {
    return extractClaim(token, claims -> claims.get("tokenType").toString());
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
      .setSigningKey(getSignKey())
      .build()
      .parseClaimsJws(token)
      .getBody();
  }

  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public Boolean validateToken(String token, String username) {
    final String usernameToken = extractUsername(token);
    return (usernameToken.equals(username) && !isTokenExpired(token));
  }

  public Boolean validateToken(String token) {
    return !isTokenExpired(token);
  }
}
