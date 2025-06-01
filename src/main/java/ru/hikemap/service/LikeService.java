package ru.hikemap.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hikemap.dto.response.HikeResponse;
import ru.hikemap.entity.Hike.Hike;
import ru.hikemap.entity.Like;
import ru.hikemap.entity.User;
import ru.hikemap.mapper.HikeMapper;
import ru.hikemap.repository.LikeRepository;
import ru.hikemap.repository.hike.HikeRepository;
import ru.hikemap.service.auth.AuthService;

@Service
@RequiredArgsConstructor
public class LikeService {

  public final HikeRepository hikeRepository;
  public final LikeRepository likeRepository;
  public final AuthService authService;
  private final HikeMapper hikeMapper;

  public void likeHike(Long hikeId) {
    // Проверка существования HikeType
    Hike hike = hikeRepository
      .findById(hikeId)
      .orElseThrow(() -> new IllegalArgumentException("Hike type not found"));

    Optional<Like> like = likeRepository.findByHikeId(hike);

    if (like.isPresent()) {
      // Удаляем лайк, если он уже существует
      likeRepository.delete(like.get());
    } else {
      // Создаем новый лайк
      Like newLike = new Like();
      newLike.setHikeId(hike);
      newLike.setUserId(authService.getCurrentUser());
      likeRepository.save(newLike);
    }
  }

  public List<HikeResponse> getLikes() {
    User user = authService.getCurrentUser();
    List<Like> likes = likeRepository.findAllByUserId(user);
    return likes
      .stream()
      .map(like -> hikeMapper.toResponse(like.getHikeId()))
      .toList();
  }
}
