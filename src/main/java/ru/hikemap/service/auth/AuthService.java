package ru.hikemap.service.auth;

import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.hikemap.entity.User;
import ru.hikemap.exception.exceptions.UnauthorizedException;
import ru.hikemap.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

  private final UserRepository userRepository;
  private final TokenBlacklistService tokenBlacklistService;
  private final JwtService jwtService;

  @Override
  public UserDetails loadUserByUsername(String username)
    throws UsernameNotFoundException {
    Optional<User> userDetail = userRepository.findByEmail(username); // Assuming 'email' is used as username

    // Converting UserInfo to UserDetails
    return userDetail
      .map(UserInfoDetails::new)
      .orElseThrow(() ->
        new UsernameNotFoundException("User not found: " + username)
      );
  }

  public User getCurrentUser() {
    Object principal = SecurityContextHolder.getContext()
      .getAuthentication()
      .getPrincipal();

    if (principal instanceof UserInfoDetails) {
      UserInfoDetails userInfoDetails = (UserInfoDetails) principal;
      return userRepository
        .findById(userInfoDetails.getId())
        .orElseThrow(() -> new IllegalArgumentException("User not found"));
    } else if (principal instanceof String) {
      // Если principal - это строка (email), загружаем пользователя из базы
      return userRepository
        .findByEmail((String) principal)
        .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
    throw new IllegalStateException("Unexpected principal type");
  }

  public Map<String, String> refreshTokens(String refreshToken) {
    // Валидация refresh token
    validateRefreshToken(refreshToken);

    // Извлекаем email из токена
    String email = jwtService.extractUsername(refreshToken);

    // Находим пользователя в БД
    User user = userRepository
      .findByEmail(email)
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    // Генерируем новые токены
    String newAccessToken = jwtService.generateAccessToken(email);
    String newRefreshToken = jwtService.generateRefreshToken(email);

    // Добавляем старый refresh token в черный список
    tokenBlacklistService.addTokenToBlacklist(
      refreshToken,
      jwtService.extractExpiration(refreshToken).getTime()
    );

    return Map.of(
      "accessToken",
      newAccessToken,
      "refreshToken",
      newRefreshToken
    );
  }

  private void validateRefreshToken(String refreshToken) {
    if (refreshToken == null || refreshToken.isBlank()) {
      throw new IllegalArgumentException("Refresh token is empty");
    }

    if (!"REFRESH".equals(jwtService.extractTokenType(refreshToken))) {
      throw new UnauthorizedException("Invalid token type");
    }

    if (!jwtService.validateToken(refreshToken)) {
      throw new UnauthorizedException("Invalid or expired refresh token");
    }

    if (tokenBlacklistService.isTokenBlacklisted(refreshToken)) {
      throw new UnauthorizedException("Refresh token was revoked");
    }
  }

  public void logout(String token) {
    // Проверяем, что токен действительный (не истек и валидный)
    if (token != null && !token.isBlank()) {
      try {
        // Извлекаем email из токена для логирования или дополнительных проверок
        String username = jwtService.extractUsername(token);

        // Добавляем токен в черный список с его реальным временем истечения
        long expirationTime = jwtService.extractExpiration(token).getTime();
        tokenBlacklistService.addTokenToBlacklist(token, expirationTime);
        // Логирование для отладки
      } catch (Exception e) {
        // Можно выбросить кастомное исключение, если нужно
      }
    }

    // Очищаем контекст безопасности в любом случае
    SecurityContextHolder.clearContext();
  }
}
