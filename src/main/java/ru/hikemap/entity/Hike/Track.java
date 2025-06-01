package ru.hikemap.entity.Hike;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.geolatte.geom.LineString;

@Getter
@Setter
@Accessors(chain = false)
@Entity
@Table(name = "track")
public class Track {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "light_track", columnDefinition = "geometry(LineString,4326)")
  private LineString LightTrack;

  @Column(name = "full_track", columnDefinition = "geometry(LineString,4326)")
  private LineString FullTrack;
}
