package ru.hikemap.repository.hike;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.hikemap.entity.Hike.Hike;

@Repository
public interface HikeRepository
  extends JpaRepository<Hike, Long>, JpaSpecificationExecutor<Hike> {
  List<Hike> findAll();
}
