package ru.hikemap.model.Hike;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.locationtech.jts.geom.LineString;
import ru.hikemap.model.User;

@Entity
@Table(name = "hike")
public class Hike {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
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
