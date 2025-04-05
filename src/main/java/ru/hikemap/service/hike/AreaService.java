package ru.hikemap.service.hike;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hikemap.dto.request.AreaRequest;
import ru.hikemap.dto.response.AreaResponse;
import ru.hikemap.entity.Hike.Area;
import ru.hikemap.exception.exceptions.ConflictException;
import ru.hikemap.exception.exceptions.ResourceNotFoundException;
import ru.hikemap.mapper.AreaMapper;
import ru.hikemap.repository.hike.AreaRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class AreaService {

  private final AreaRepository areaRepository;
  private final AreaMapper areaMapper;

  public AreaResponse createArea(AreaRequest newArea) {
    //todo: check on an empty field
    if (areaRepository.existsByName(newArea.name())) {
      throw new ConflictException(
        "Area with name '" + newArea.name() + "' already exists"
      );
    }
    Area saveArea = areaRepository.save(areaMapper.toEntity(newArea));
    return areaMapper.toResponse(saveArea);
  }

  @Transactional(readOnly = true)
  public List<AreaResponse> getAllAreas() {
    return areaRepository
      .findAll()
      .stream()
      .map(areaMapper::toResponse)
      .toList();
  }

  @Transactional(readOnly = true)
  public AreaResponse getAreaById(Long id) {
    Area area = areaRepository
      .findById(id)
      .orElseThrow(() ->
        new ResourceNotFoundException("Area not found with id: " + id)
      );
    return areaMapper.toResponse(area);
  }

  public AreaResponse updateArea(Long id, AreaRequest areaUpdate) {
    Area area = areaRepository
      .findById(id)
      .orElseThrow(() ->
        new ResourceNotFoundException("Area not found with id: " + id)
      );

    if (!areaUpdate.id().equals(id)) {
      if (areaRepository.existsById(areaUpdate.id())) {
        throw new ConflictException(
          "Area name '" + areaUpdate.name() + "' already exists"
        );
      }
      area.setName(areaUpdate.name());
    }

    return areaMapper.toResponse(areaRepository.save(area));
  }

  public void deleteArea(Long id) {
    if (!areaRepository.existsById(id)) {
      throw new ResourceNotFoundException("Area not found with id: " + id);
    }
    areaRepository.deleteById(id);
  }
}
