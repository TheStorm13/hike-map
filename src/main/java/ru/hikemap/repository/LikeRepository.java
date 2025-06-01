package ru.hikemap.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hikemap.entity.Hike.Hike;
import ru.hikemap.entity.Like;
import ru.hikemap.entity.User;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
  Optional<Like> findByHikeId(Hike hike);

  List<Like> findAllByUserId(User user);
}
