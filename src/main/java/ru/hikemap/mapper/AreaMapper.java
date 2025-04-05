package ru.hikemap.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.hikemap.dto.request.AreaRequest;
import ru.hikemap.dto.response.AreaResponse;
import ru.hikemap.entity.Hike.Area;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AreaMapper {
  AreaResponse toResponse(Area area);

  Area toEntity(AreaRequest areaRequest);
}
