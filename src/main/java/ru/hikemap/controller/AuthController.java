package ru.hikemap.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hikemap.dto.request.AuthRequest;
import ru.hikemap.dto.request.UserRequest;
import ru.hikemap.service.UserService;
import ru.hikemap.service.auth.AuthService;
import ru.hikemap.service.auth.JwtService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final UserService service;
  private final JwtService jwtService;
  private final AuthService authService;

  @PostMapping("/register")
  public void registerUser(@RequestBody UserRequest userRequest) {
    service.createUser(userRequest);
  }

  @PostMapping("/login")
  public Map<String, String> authenticateAndGetToken(
    @RequestBody AuthRequest authRequest
  ) {
    Authentication authentication = authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        authRequest.email(),
        authRequest.password()
      )
    );
    if (authentication.isAuthenticated()) {
      return Map.of(
        "accessToken",
        jwtService.generateAccessToken(authRequest.email()),
        "refreshToken",
        jwtService.generateRefreshToken(authRequest.email())
      );
    } else {
      throw new UsernameNotFoundException("Invalid user request!");
    }
  }

  @PostMapping("/refresh")
  public ResponseEntity<?> refreshToken(@RequestBody String refreshToken) {
    try {
      Map<String, String> tokens = authService.refreshTokens(refreshToken);
      return ResponseEntity.ok(tokens);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
        Map.of("error", e.getMessage())
      );
    }
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(HttpServletRequest request) {
    String token = extractToken(request); // Ваш метод извлечения токена из запроса
    authService.logout(token);
    return ResponseEntity.ok().build();
  }

  private String extractToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}
