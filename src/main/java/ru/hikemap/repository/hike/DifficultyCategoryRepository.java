package ru.hikemap.repository.hike;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hikemap.entity.Hike.DifficultyCategory;

@Repository
public interface DifficultyCategoryRepository
  extends JpaRepository<DifficultyCategory, Long> {
  Optional<DifficultyCategory> findById(Long id);

  boolean existsByName(String name);
}
