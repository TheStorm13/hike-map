package ru.hikemap.service.auth;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Service;

@Service
public class TokenBlacklistService {

  private final ConcurrentMap<String, Long> blacklist =
    new ConcurrentHashMap<>();

  public void addTokenToBlacklist(String token, long expirationTime) {
    cleanExpiredTokens();
    blacklist.put(token, expirationTime);
  }

  public boolean isTokenBlacklisted(String token) {
    Long expirationTime = blacklist.get(token);
    if (expirationTime == null) {
      return false;
    }
    // Если время истекло, удаляем из черного списка
    if (System.currentTimeMillis() > expirationTime) {
      blacklist.remove(token);
      return false;
    }
    return true;
  }

  private void cleanExpiredTokens() {
    long currentTime = System.currentTimeMillis();
    blacklist.entrySet().removeIf(entry -> entry.getValue() < currentTime);
  }
}
