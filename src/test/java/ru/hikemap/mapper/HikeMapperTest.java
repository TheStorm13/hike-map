package ru.hikemap.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.geolatte.geom.G2D;
import org.geolatte.geom.LineString;
import org.geolatte.geom.PositionSequenceBuilders;
import org.geolatte.geom.crs.CoordinateReferenceSystems;
import org.junit.jupiter.api.Test;
import ru.hikemap.dto.response.HikeResponse;
import ru.hikemap.entity.Hike.Area;
import ru.hikemap.entity.Hike.DifficultyCategory;
import ru.hikemap.entity.Hike.Hike;
import ru.hikemap.entity.Hike.HikeType;
import ru.hikemap.entity.User;

public class HikeMapperTest {

  private final HikeMapper hikeMapper = new HikeMapperImpl();

  @Test
  void toResponseShouldMapAllFieldsCorrectly() {
    // Given
    User organizer = new User();
    organizer.setId(1L);

    Area area = new Area();
    area.setId(1L);

    DifficultyCategory difficulty = new DifficultyCategory();
    difficulty.setId(1L);

    HikeType hikeType = new HikeType();
    hikeType.setId(1L);

    Hike hike = new Hike();
    hike.setId(1L);
    hike.setTitle("Test Hike");
    hike.setDescription("Test Description");
    hike.setPhotoPath("/photos/test.jpg");
    hike.setStartDate(LocalDate.of(2023, 1, 1));
    hike.setEndDate(LocalDate.of(2023, 1, 5));
    hike.setTrackGpxPath("/tracks/test.gpx");
    hike.setTrackGeometry(createTestLineString());
    hike.setReportPdfPath("/reports/test.pdf");
    hike.setCreatedAt(LocalDateTime.now());
    hike.setUpdatedAt(LocalDateTime.now());
    hike.setOrganizer(organizer);
    hike.setArea(area);
    hike.setDifficulty(difficulty);
    hike.setHikeType(hikeType);

    // When
    HikeResponse response = hikeMapper.toResponse(hike);

    // Then
    assertThat(hike.getTitle()).isEqualTo("Test Hike");

    assertThat(response.id()).isEqualTo(hike.getId());
    assertThat(response.title()).isEqualTo(hike.getTitle());
    assertThat(response.photoPath()).isEqualTo(hike.getPhotoPath());
    assertThat(response.startDate()).isEqualTo(hike.getStartDate());
    assertThat(response.endDate()).isEqualTo(hike.getEndDate());
    assertThat(response.trackGpxPath()).isEqualTo(hike.getTrackGpxPath());
    assertThat(response.trackGeometry()).isEqualTo(hike.getTrackGeometry());
    assertThat(response.reportPdfPath()).isEqualTo(hike.getReportPdfPath());
    assertThat(response.organizer()).isEqualTo(hike.getOrganizer());
    assertThat(response.area()).isEqualTo(hike.getArea());
    assertThat(response.difficulty()).isEqualTo(hike.getDifficulty());
    assertThat(response.hikeType()).isEqualTo(hike.getHikeType());
  }

  private LineString createTestLineString() {
    return new LineString<>(
      PositionSequenceBuilders.fixedSized(2, G2D.class)
        .add(new G2D(0, 0))
        .add(new G2D(1, 1))
        .toPositionSequence(),
      CoordinateReferenceSystems.WGS84
    );
  }
}
