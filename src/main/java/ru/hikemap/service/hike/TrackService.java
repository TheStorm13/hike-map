package ru.hikemap.service.hike;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.geolatte.geom.G2D;
import org.geolatte.geom.PositionSequence;
import org.springframework.stereotype.Service;
import ru.hikemap.dto.response.HikeGeoJsonResponse;
import ru.hikemap.entity.Hike.Hike;
import ru.hikemap.repository.hike.HikeRepository;

@Service
@RequiredArgsConstructor
public class TrackService {

  public final HikeRepository hikeRepository;

  public HikeGeoJsonResponse getTracks(List<Long> hikeIds) {
    List<Hike> hikes = hikeRepository.findAllById(hikeIds);

    List<Map<String, Object>> features = hikes
      .stream()
      .filter(hike -> hike.getTrackGeometry() != null)
      .map(hike -> {
        List<List<Double>> coordinates = new ArrayList<>();
        PositionSequence<G2D> positions = hike
          .getTrackGeometry()
          .getPositions();
        for (int i = 0; i < positions.size(); i++) {
          G2D pos = positions.getPositionN(i);
          coordinates.add(List.of(pos.getLon(), pos.getLat()));
        }
        return Map.of(
          "type",
          "Feature",
          "geometry",
          Map.of("type", "LineString", "coordinates", coordinates),
          "properties",
          Map.of(
            "id",
            hike.getId(),
            "title",
            hike.getTitle(),
            "description",
            hike.getDescription()
          )
        );
      })
      .collect(Collectors.toList());

    return new HikeGeoJsonResponse("FeatureCollection", features);
  }
}
