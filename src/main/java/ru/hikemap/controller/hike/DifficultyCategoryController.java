package ru.hikemap.controller.hike;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hikemap.dto.request.DifficultyCategoryRequest;
import ru.hikemap.dto.response.DifficultyCategoryResponse;
import ru.hikemap.exception.exceptions.ValidationException;
import ru.hikemap.service.hike.DifficultyCategoryService;

@RestController
@RequestMapping("/hikes/diff")
@RequiredArgsConstructor
public class DifficultyCategoryController {

  private final DifficultyCategoryService difficultyCategoryService;

  @PostMapping
  public ResponseEntity<DifficultyCategoryResponse> createDifficultyCategory(
    @RequestBody DifficultyCategoryRequest difficultyCategoryRequest
  ) {
    return ResponseEntity.ok(
      difficultyCategoryService.createDifficultyCategory(
        difficultyCategoryRequest
      )
    );
  }

  @GetMapping
  public ResponseEntity<
    List<DifficultyCategoryResponse>
  > getAllDifficultyCategory() {
    return ResponseEntity.ok(
      difficultyCategoryService.getAllDifficultyCategory()
    );
  }

  @GetMapping("/{id}")
  public ResponseEntity<DifficultyCategoryResponse> getDifficultyCategoryById(
    @PathVariable Long id
  ) {
    return ResponseEntity.ok(
      difficultyCategoryService.getDifficultyCategoryById(id)
    );
  }

  @PutMapping("/{id}")
  public ResponseEntity<DifficultyCategoryResponse> updateDifficultyCategory(
    @PathVariable Long id,
    @RequestBody DifficultyCategoryRequest difficultyCategoryRequest
  ) {
    if (!id.equals(difficultyCategoryRequest.id())) {
      throw new ValidationException("Path ID and body ID must match");
    }
    return ResponseEntity.ok(
      difficultyCategoryService.updateDifficultyCategory(
        id,
        difficultyCategoryRequest
      )
    );
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteDifficultyCategory(@PathVariable Long id) {
    difficultyCategoryService.deleteDifficultyCategory(id);
    return ResponseEntity.noContent().build();
  }
}
