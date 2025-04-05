package ru.hikemap.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.hikemap.dto.request.HikeTypeRequest;
import ru.hikemap.dto.response.HikeTypeResponse;
import ru.hikemap.entity.Hike.HikeType;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HikeTypeMapper {
  HikeTypeResponse toResponse(HikeType hikeType);

  HikeType toEntity(HikeTypeRequest hikeTypeRequest);
}
