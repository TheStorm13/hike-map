package ru.hikemap.service.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  @Lazy
  private final UserDetailsService userDetailsService;

  private final TokenBlacklistService tokenBlacklistService;
  private final JwtService jwtService;

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    String authHeader = request.getHeader("Authorization");
    String token = null;
    String username = null;
    String tokenType = null;

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      token = authHeader.substring(7);
      username = jwtService.extractUsername(token);
      tokenType = jwtService.extractTokenType(token);
    }

    if (
      username != null &&
      SecurityContextHolder.getContext().getAuthentication() == null &&
      tokenType.equals("accessToken") &&
      !tokenBlacklistService.isTokenBlacklisted(token)
    ) {
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      if (jwtService.validateToken(token, userDetails.getUsername())) {
        UsernamePasswordAuthenticationToken authToken =
          new UsernamePasswordAuthenticationToken(
            userDetails, // Важно передать userDetails, а не username
            null,
            userDetails.getAuthorities()
          );
        authToken.setDetails(
          new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }
    filterChain.doFilter(request, response);
  }
}
