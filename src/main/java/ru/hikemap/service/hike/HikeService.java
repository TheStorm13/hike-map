package ru.hikemap.service.hike;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hikemap.dto.request.HikeRequest;
import ru.hikemap.dto.response.HikeResponse;
import ru.hikemap.dto.response.UserResponse;
import ru.hikemap.entity.Hike.Area;
import ru.hikemap.entity.Hike.Hike;
import ru.hikemap.entity.Hike.HikeType;
import ru.hikemap.entity.User;
import ru.hikemap.exception.exceptions.ResourceNotFoundException;
import ru.hikemap.exception.exceptions.ValidationException;
import ru.hikemap.mapper.HikeMapper;
import ru.hikemap.mapper.UserMapper;
import ru.hikemap.repository.UserRepository;
import ru.hikemap.repository.hike.AreaRepository;
import ru.hikemap.repository.hike.HikeRepository;
import ru.hikemap.repository.hike.HikeTypeRepository;

@Service
@RequiredArgsConstructor
public class HikeService {

  private final HikeRepository hikeRepository;
  private final UserRepository userRepository;
  private final AreaRepository areaRepository;
  private final HikeTypeRepository hikeTypeRepository;

  private final HikeMapper hikeMapper;
  private final UserMapper userMapper;

  @Transactional(readOnly = true)
  public List<HikeResponse> getAllHikes() {
    //todo: add pagination
    return hikeRepository
      .findAll()
      .stream()
      .map(hikeMapper::toResponse)
      .toList();
  }

  @Transactional
  public Long createHike(HikeRequest hikeRequest) {
    // Проверка существования Area
    Area area = areaRepository
      .findById(hikeRequest.areaId())
      .orElseThrow(() -> new IllegalArgumentException("Area not found"));

    // Проверка существования HikeType
    HikeType hikeType = hikeTypeRepository
      .findById(hikeRequest.hikeTypeId())
      .orElseThrow(() -> new IllegalArgumentException("Hike type not found"));

    User organizer = userRepository
      .findById(hikeRequest.organizerId())
      .orElseThrow(() -> new IllegalArgumentException("Organizer not found"));

    Hike hike = hikeMapper.toEntity(hikeRequest);

    hike.setArea(area);
    hike.setHikeType(hikeType);
    hike.setOrganizer(organizer);

    hikeRepository.save(hike);
    return hike.getId();
  }

  @Transactional
  public HikeResponse updateHike(
    Long id,
    String title,
    String description,
    LocalDate startDate,
    LocalDate endDate
  ) {
    Hike existingHike = getHikeEntity(id);
    validateInput(title, startDate, endDate);

    updateHikeFields(existingHike, title, description, startDate, endDate);
    //updateFilePaths(existingHike, photo, trackGpx, reportPdf);

    Hike updatedHike = hikeRepository.save(existingHike);
    return hikeMapper.toResponse(updatedHike);
  }

  @Transactional
  public void deleteHike(Long id) {
    Hike hike = getHikeEntity(id);
    //deleteAssociatedFiles(hike);
    hikeRepository.delete(hike);
  }

  @Transactional(readOnly = true)
  public HikeResponse getHike(Long hikeId) {
    return hikeMapper.toResponse(getHikeEntity(hikeId));
  }

  public List<HikeResponse> getFilteredHikes(
    LocalDate startDateFrom,
    LocalDate startDateTo,
    Integer difficulty,
    Boolean isCategorical,
    Long areaId,
    Long hikeTypeId,
    Long organizerId
  ) {
    Specification<Hike> spec = Specification.where(null);

    if (startDateFrom != null) {
      spec = spec.and((root, query, cb) ->
        cb.greaterThanOrEqualTo(root.get("startDate"), startDateFrom)
      );
    }

    if (startDateTo != null) {
      spec = spec.and((root, query, cb) ->
        cb.lessThanOrEqualTo(root.get("startDate"), startDateTo)
      );
    }

    if (difficulty != null) {
      spec = spec.and((root, query, cb) ->
        cb.equal(root.get("difficulty"), difficulty)
      );
    }

    if (isCategorical != null) {
      spec = spec.and((root, query, cb) ->
        cb.equal(root.get("isCategorical"), isCategorical)
      );
    }

    if (areaId != null) {
      spec = spec.and((root, query, cb) ->
        cb.equal(root.get("area").get("id"), areaId)
      );
    }

    if (hikeTypeId != null) {
      spec = spec.and((root, query, cb) ->
        cb.equal(root.get("hikeType").get("id"), hikeTypeId)
      );
    }

    if (organizerId != null) {
      spec = spec.and((root, query, cb) ->
        cb.equal(root.get("organizer").get("id"), organizerId)
      );
    }

    List<Hike> hikes = hikeRepository.findAll(spec);

    return hikes
      .stream()
      .map(hike ->
        new HikeResponse(
          hike.getId(),
          hike.getTitle(),
          hike.getDescription(),
          hike.getPhotoPath(),
          hike.getTrackGpxPath(),
          hike.getReportPdfPath(),
          hike.getStartDate(),
          hike.getEndDate(),
          hike.getDifficulty(),
          hike.isCategorical(),
          hike.getArea().getName(), // предполагается, что у Area есть поле name
          hike.getHikeType().getName(), // предполагается, что у HikeType есть поле name
          hike.getOrganizer().getUsername() // предполагается, что у User есть поле username
        )
      )
      .toList();
  }

  private Hike getHikeEntity(Long id) {
    return hikeRepository
      .findById(id)
      .orElseThrow(() ->
        new ResourceNotFoundException("Hike not found with id: " + id)
      );
  }

  private void validateInput(
    String title,
    LocalDate startDate,
    LocalDate endDate
  ) {
    if (title == null || title.isBlank()) {
      throw new ValidationException("Title is required");
    }
    if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
      throw new ValidationException("Invalid date range");
    }
  }

  private void updateHikeFields(
    Hike hike,
    String title,
    String description,
    LocalDate startDate,
    LocalDate endDate
  ) {
    hike
      .setTitle(title)
      .setDescription(description)
      .setStartDate(startDate)
      .setEndDate(endDate);
  }

  public List<UserResponse> getAllOrganizer() {
    return hikeRepository
      .findAll()
      .stream()
      .map(Hike::getOrganizer)
      .distinct()
      .map(userMapper::toResponse)
      .toList();
  }
}
