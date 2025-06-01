package ru.hikemap.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.hikemap.dto.request.HikeRequest;
import ru.hikemap.dto.response.HikeResponse;
import ru.hikemap.entity.Hike.Hike;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HikeMapper {
  @Mapping(target = "area", source = "hike.area.name") // например, берем название региона
  @Mapping(target = "hikeType", source = "hike.hikeType.name") // берем строковое значение типа
  @Mapping(target = "organizer", source = "hike.organizer.username")
  HikeResponse toResponse(Hike hike);

  Hike toEntity(HikeRequest hikeRequset);
}
