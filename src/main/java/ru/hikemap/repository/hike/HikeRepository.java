package ru.hikemap.repository.hike;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.hikemap.entity.Hike.Hike;

@Repository
public interface HikeRepository extends JpaRepository<Hike, Long> {
  List<Hike> findAll();

  @Query(
    value = "SELECT h FROM Hike h WHERE " +
    "(:title IS NULL OR h.title LIKE %:title%) AND " +
    "(:startDate IS NULL OR h.startDate >= :startDate) AND " +
    "(:endDate IS NULL OR h.endDate <= :endDate) AND " +
    "(:areaId IS NULL OR h.area.id = :areaId) AND " +
    "(:typeId IS NULL OR h.hikeType.id = :typeId) AND " +
    "(:difficultyId IS NULL OR h.difficulty.id = :difficultyId)",
    nativeQuery = true
  )
  List<Hike> findHikesByFilters(
    @Param("title") String title,
    @Param("startDate") LocalDate startDate,
    @Param("endDate") LocalDate endDate,
    @Param("areaId") Long areaId,
    @Param("typeId") Long typeId,
    @Param("difficultyId") Long difficultyId
  );

  @Query(
    value = "SELECT * FROM hike " +
    "WHERE ST_DWithin(track_geometry, ST_SetSRID(ST_MakePoint(:lon, :lat), 4326), :distance)",
    nativeQuery = true
  )
  List<Hike> findHikesNearLocation(
    @Param("lat") double lat,
    @Param("lon") double lon,
    @Param("distance") double distance
  );
}
