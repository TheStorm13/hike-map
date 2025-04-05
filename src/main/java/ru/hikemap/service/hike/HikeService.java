package ru.hikemap.service.hike;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hikemap.dto.response.HikeResponse;
import ru.hikemap.entity.Hike.Hike;
import ru.hikemap.exception.exceptions.ResourceNotFoundException;
import ru.hikemap.exception.exceptions.ValidationException;
import ru.hikemap.mapper.HikeMapper;
import ru.hikemap.repository.hike.HikeRepository;

@Service
@RequiredArgsConstructor
public class HikeService {

  private final HikeRepository hikeRepository;
  private final HikeMapper hikeMapper;

  //private final FileStorageService fileStorageService;

  @Transactional(readOnly = true)
  public List<HikeResponse> getAllHikes() {
    //    @Valid HikeFilterCriteria filterCriteria,
    //    @PageableDefault(size = 20) Pageable pageable
    //  ) {
    //    HikeSearchSpecification spec = new HikeSearchSpecification(filterCriteria);
    //    Page<Hike> hikes = hikeService.searchHikes(spec, pageable);
    //    return hikeMapper.toPageResponse(hikes, userDetails);
    //todo: add pagination
    return hikeRepository
      .findAll()
      .stream()
      .map(hikeMapper::toResponse)
      .toList();
  }

  @Transactional
  public HikeResponse createHike(
    String title,
    String description,
    LocalDate startDate,
    LocalDate endDate,
    MultipartFile photo,
    MultipartFile trackGpx,
    MultipartFile reportPdf
  ) {
    validateInput(title, startDate, endDate);

    Hike hike = new Hike()
      .setTitle(title)
      .setDescription(description)
      .setStartDate(startDate)
      .setEndDate(endDate); //.setReportPdfPath(processFile(reportPdf)) // .setPhotoPath(processFile(photo)) //.setTrackGpxPath(processFile(trackGpx))

    Hike savedHike = hikeRepository.save(hike);
    return hikeMapper.toResponse(savedHike);
  }

  @Transactional
  public HikeResponse updateHike(
    Long id,
    String title,
    String description,
    LocalDate startDate,
    LocalDate endDate,
    MultipartFile photo,
    MultipartFile trackGpx,
    MultipartFile reportPdf
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
}
