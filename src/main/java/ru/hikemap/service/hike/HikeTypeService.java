package ru.hikemap.service.hike;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hikemap.dto.request.HikeTypeRequest;
import ru.hikemap.dto.response.HikeTypeResponse;
import ru.hikemap.entity.Hike.HikeType;
import ru.hikemap.exception.exceptions.ConflictException;
import ru.hikemap.exception.exceptions.ResourceNotFoundException;
import ru.hikemap.mapper.HikeTypeMapper;
import ru.hikemap.repository.hike.HikeTypeRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class HikeTypeService {

  private final HikeTypeRepository hikeTypeRepository;
  private final HikeTypeMapper hikeTypeMapper;

  public HikeTypeResponse createHikeType(HikeTypeRequest hikeTypeRequest) {
    //todo: check on an empty field
    if (hikeTypeRepository.existsByName(hikeTypeRequest.name())) {
      throw new ConflictException(
        "HikeType with name '" + hikeTypeRequest.name() + "' already exists"
      );
    }
    HikeType saveHikeType = hikeTypeRepository.save(
      hikeTypeMapper.toEntity(hikeTypeRequest)
    );
    return hikeTypeMapper.toResponse(saveHikeType);
  }

  @Transactional(readOnly = true)
  public List<HikeTypeResponse> getAllHikeType() {
    return hikeTypeRepository
      .findAll()
      .stream()
      .map(hikeTypeMapper::toResponse)
      .toList();
  }

  @Transactional(readOnly = true)
  public HikeTypeResponse getHikeTypeById(Long id) {
    HikeType hikeType = hikeTypeRepository
      .findById(id)
      .orElseThrow(() ->
        new ResourceNotFoundException("HikeType not found with id: " + id)
      );
    return hikeTypeMapper.toResponse(hikeType);
  }

  public HikeTypeResponse updateHikeType(
    Long id,
    HikeTypeRequest hikeTypeUpdate
  ) {
    HikeType hikeType = hikeTypeRepository
      .findById(id)
      .orElseThrow(() ->
        new ResourceNotFoundException("HikeType not found with id: " + id)
      );

    if (!hikeTypeUpdate.id().equals(id)) {
      if (hikeTypeRepository.existsById(hikeTypeUpdate.id())) {
        throw new ConflictException(
          "HikeType name '" + hikeTypeUpdate.name() + "' already exists"
        );
      }
      hikeType.setName(hikeTypeUpdate.name());
    }

    return hikeTypeMapper.toResponse(hikeTypeRepository.save(hikeType));
  }

  public void deleteHikeType(Long id) {
    if (!hikeTypeRepository.existsById(id)) {
      throw new ResourceNotFoundException("HikeType not found with id: " + id);
    }
    hikeTypeRepository.deleteById(id);
  }
}
