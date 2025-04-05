package ru.hikemap.repository.hike;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hikemap.entity.Hike.HikeType;

@Repository
public interface HikeTypeRepository extends JpaRepository<HikeType, Long> {
  Optional<HikeType> findById(Long id);

  boolean existsByName(String name);
}
