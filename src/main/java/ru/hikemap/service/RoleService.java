package ru.hikemap.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hikemap.entity.Role;
import ru.hikemap.repository.RoleRepository;

@Service
@RequiredArgsConstructor
public class RoleService {

  private final RoleRepository roleRepository;

  //  public String getUserRole(Long userId) {
  //    User user = userService.getUserById(userId);
  //    if (user == null || user.getRole() == null) {
  //      throw new IllegalArgumentException(
  //        "User or role not found for ID: " + userId
  //      );
  //    }
  //    return user.getRole().getName();
  //  }
  public Role getRole(String roleName) {
    return roleRepository.findByName(roleName);
  }
}
