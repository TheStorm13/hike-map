package ru.hikemap.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.hikemap.dto.response.HikeResponse;
import ru.hikemap.service.LikeService;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikeController {

  public final LikeService likeService;

  @PreAuthorize("hasAuthority('member')")
  @PutMapping("/{hikeId}")
  public void likeHike(@PathVariable Long hikeId) {
    likeService.likeHike(hikeId);
  }

  @PreAuthorize("hasAuthority('member')")
  @GetMapping
  public List<HikeResponse> getLikes() {
    return likeService.getLikes();
  }
}
