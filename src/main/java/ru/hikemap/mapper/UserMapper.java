package ru.hikemap.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.hikemap.dto.request.AuthRequest;
import ru.hikemap.dto.request.UserRequest;
import ru.hikemap.dto.response.UserResponse;
import ru.hikemap.entity.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
  UserResponse toResponse(User user);

  User toEntity(UserRequest userRequest);

  List<UserResponse> toResponse(List<User> user);

  List<User> toEntity(List<UserRequest> userRequest);

  User toEntity(AuthRequest authRequest);
}
