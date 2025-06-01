package ru.hikemap.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.hikemap.dto.request.UserRequest;
import ru.hikemap.dto.response.UserResponse;
import ru.hikemap.service.UserService;
import ru.hikemap.service.hike.HikeService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final HikeService hikeService;

  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
    return ResponseEntity.ok(userService.getUserById(id));
  }

  @GetMapping("/organizers")
  public ResponseEntity<List<UserResponse>> getAllOrganizer() {
    List<UserResponse> users = hikeService.getAllOrganizer();
    return ResponseEntity.ok(users);
  }

  @GetMapping
  public ResponseEntity<List<UserResponse>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());
  }

  @PreAuthorize("hasAuthority('member')")
  @PutMapping("/{id}")
  public ResponseEntity<UserResponse> updateUser(
    @PathVariable Long id,
    @RequestBody UserRequest user
  ) {
    return ResponseEntity.ok(userService.updateUser(id, user));
  }

  @PreAuthorize("hasAuthority('admin')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }
}
