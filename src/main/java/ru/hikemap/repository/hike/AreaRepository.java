package ru.hikemap.repository.hike;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hikemap.entity.Hike.Area;

@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {
  boolean existsByName(String name);

  Optional<Area> findById(Long id);
}
