package ru.hikemap.service.hike;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hikemap.dto.request.DifficultyCategoryRequest;
import ru.hikemap.dto.response.DifficultyCategoryResponse;
import ru.hikemap.entity.Hike.DifficultyCategory;
import ru.hikemap.exception.exceptions.ConflictException;
import ru.hikemap.exception.exceptions.ResourceNotFoundException;
import ru.hikemap.mapper.DifficultyCategoryMapper;
import ru.hikemap.repository.hike.DifficultyCategoryRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class DifficultyCategoryService {

  private final DifficultyCategoryRepository difficultyCategoryRepository;
  private final DifficultyCategoryMapper difficultyCategoryMapper;

  public DifficultyCategoryResponse createDifficultyCategory(
    DifficultyCategoryRequest difficultyCategoryRequest
  ) {
    //todo: check on an empty field
    if (
      difficultyCategoryRepository.existsByName(
        difficultyCategoryRequest.name()
      )
    ) {
      throw new ConflictException(
        "Area with name '" +
        difficultyCategoryRequest.name() +
        "' already exists"
      );
    }
    DifficultyCategory saveDifficultyCategory =
      difficultyCategoryRepository.save(
        difficultyCategoryMapper.toEntity(difficultyCategoryRequest)
      );
    return difficultyCategoryMapper.toResponse(saveDifficultyCategory);
  }

  @Transactional(readOnly = true)
  public List<DifficultyCategoryResponse> getAllDifficultyCategory() {
    return difficultyCategoryRepository
      .findAll()
      .stream()
      .map(difficultyCategoryMapper::toResponse)
      .toList();
  }

  @Transactional(readOnly = true)
  public DifficultyCategoryResponse getDifficultyCategoryById(Long id) {
    DifficultyCategory difficultyCategory = difficultyCategoryRepository
      .findById(id)
      .orElseThrow(() ->
        new ResourceNotFoundException("Area not found with id: " + id)
      );
    return difficultyCategoryMapper.toResponse(difficultyCategory);
  }

  public DifficultyCategoryResponse updateDifficultyCategory(
    Long id,
    DifficultyCategoryRequest areaUpdate
  ) {
    DifficultyCategory difficultyCategory = difficultyCategoryRepository
      .findById(id)
      .orElseThrow(() ->
        new ResourceNotFoundException("Area not found with id: " + id)
      );

    if (!areaUpdate.id().equals(id)) {
      if (difficultyCategoryRepository.existsById(areaUpdate.id())) {
        throw new ConflictException(
          "Area name '" + areaUpdate.name() + "' already exists"
        );
      }
      difficultyCategory.setName(areaUpdate.name());
    }

    return difficultyCategoryMapper.toResponse(
      difficultyCategoryRepository.save(difficultyCategory)
    );
  }

  public void deleteDifficultyCategory(Long id) {
    if (!difficultyCategoryRepository.existsById(id)) {
      throw new ResourceNotFoundException("Area not found with id: " + id);
    }
    difficultyCategoryRepository.deleteById(id);
  }
}
