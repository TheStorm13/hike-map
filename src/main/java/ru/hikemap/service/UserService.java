package ru.hikemap.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.hikemap.dto.request.UserRequest;
import ru.hikemap.dto.response.UserResponse;
import ru.hikemap.entity.User;
import ru.hikemap.mapper.UserMapper;
import ru.hikemap.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder encoder;
  private final RoleService roleService;

  public UserResponse getUserById(Long id) {
    User user = userRepository
      .findById(id)
      .orElseThrow(() -> new IllegalArgumentException("User not found"));
    return userMapper.toResponse(user);
  }

  public List<UserResponse> getAllUsers() {
    List<User> users = userRepository.findAll();
    return userMapper.toResponse(users);
  }

  public UserResponse createUser(UserRequest userRequest) {
    User user = userMapper.toEntity(userRequest);
    user.setPasswordHash(encoder.encode(userRequest.password()));
    user.setRole(roleService.getRole("member"));
    userRepository.save(user);
    return userMapper.toResponse(user);
  }

  public UserResponse updateUser(Long id, UserRequest updatedUser) {
    User existingUser = userRepository
      .findById(id)
      .orElseThrow(() -> new IllegalArgumentException("User not found"));

    //todo:
    if (!existingUser.getUsername().equals(updatedUser.username())) {
      throw new IllegalArgumentException("Username already exists");
    }

    if (!existingUser.getEmail().equals(updatedUser.email())) {
      throw new IllegalArgumentException("Email already exists");
    }

    existingUser.setUsername(updatedUser.username());
    existingUser.setEmail(updatedUser.email());
    existingUser.setPasswordHash(updatedUser.password());

    User saveUser = userRepository.save(existingUser);
    return userMapper.toResponse(saveUser);
  }

  public void deleteUser(Long id) {
    if (!userRepository.existsById(id)) {
      throw new IllegalArgumentException("User not found");
    }
    userRepository.deleteById(id);
  }
}
