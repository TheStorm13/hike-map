package ru.hikemap.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.hikemap.dto.request.DifficultyCategoryRequest;
import ru.hikemap.dto.response.DifficultyCategoryResponse;
import ru.hikemap.entity.Hike.DifficultyCategory;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DifficultyCategoryMapper {
  DifficultyCategoryResponse toResponse(DifficultyCategory difficultyCategory);

  DifficultyCategory toEntity(
    DifficultyCategoryRequest difficultyCategoryRequest
  );
}
