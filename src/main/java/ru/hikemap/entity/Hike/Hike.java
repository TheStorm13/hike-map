package ru.hikemap.entity.Hike;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.geolatte.geom.LineString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.hikemap.entity.User;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "hike")
public class Hike {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "title")
  private String title;

  @Column(name = "description")
  private String description;

  @Column(name = "photo_path")
  private String photoPath;

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;

  @Column(name = "track_gpx_path")
  private String trackGpxPath;

  @Column(columnDefinition = "geometry(LineString,4326)")
  private LineString trackGeometry;

  @Column(name = "report_pdf_path")
  private String reportPdfPath;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "organizer_id", nullable = false)
  private User organizer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "area_id", nullable = false)
  private Area area;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "difficulty_id", nullable = false)
  private DifficultyCategory difficulty;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "hike_type_id", nullable = false)
  private HikeType hikeType;
}
