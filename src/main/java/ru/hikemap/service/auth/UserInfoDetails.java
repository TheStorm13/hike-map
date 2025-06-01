package ru.hikemap.service.auth;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.hikemap.entity.User;

public class UserInfoDetails implements UserDetails {

  private Long id;
  private String username; // Changed from 'name' to 'email' for clarity
  private String password;
  private List<GrantedAuthority> authorities;

  public UserInfoDetails(User user) {
    this.id = user.getId();
    this.username = user.getEmail(); // Use email as username
    this.password = user.getPasswordHash();
    this.authorities = List.of(
      new SimpleGrantedAuthority(user.getRole().getName())
    );
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  public Long getId() {
    return id;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
