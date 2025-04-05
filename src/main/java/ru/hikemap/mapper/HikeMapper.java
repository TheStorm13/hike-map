package ru.hikemap.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.hikemap.dto.request.HikeRequest;
import ru.hikemap.dto.response.HikeResponse;
import ru.hikemap.entity.Hike.Hike;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HikeMapper {
  HikeResponse toResponse(Hike hike);

  Hike toEntity(HikeRequest hikeRequset);
}
