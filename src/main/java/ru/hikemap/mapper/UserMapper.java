package ru.hikemap.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.hikemap.dto.request.UserRequest;
import ru.hikemap.dto.response.UserResponse;
import ru.hikemap.entity.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
  UserResponse toResponse(User user);
  User toEntity(UserRequest userRequest);
}
