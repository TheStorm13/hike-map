package ru.hikemap.controller.hike;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.hikemap.dto.request.AreaRequest;
import ru.hikemap.dto.response.AreaResponse;
import ru.hikemap.exception.exceptions.ValidationException;
import ru.hikemap.service.hike.AreaService;

@RestController
@RequestMapping("/hikes/areas")
@RequiredArgsConstructor
public class AreaController {

  private final AreaService areaService;

  @PostMapping
  @PreAuthorize("hasAuthority('admin')")
  public ResponseEntity<AreaResponse> createArea(
    @RequestBody AreaRequest areaRequest
  ) {
    return ResponseEntity.ok(areaService.createArea(areaRequest));
  }

  @GetMapping
  public ResponseEntity<List<AreaResponse>> getAllAreas() {
    return ResponseEntity.ok(areaService.getAllAreas());
  }

  @GetMapping("/{id}")
  public ResponseEntity<AreaResponse> getAreaById(@PathVariable Long id) {
    return ResponseEntity.ok(areaService.getAreaById(id));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('admin')")
  public ResponseEntity<AreaResponse> updateArea(
    @PathVariable Long id,
    @RequestBody AreaRequest areaRequest
  ) {
    //todo: transfer the condition to the service
    if (!id.equals(areaRequest.id())) {
      throw new ValidationException("Path ID and body ID must match");
    }

    return ResponseEntity.ok(areaService.updateArea(id, areaRequest));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('admin')")
  public ResponseEntity<Void> deleteArea(@PathVariable Long id) {
    areaService.deleteArea(id);
    return ResponseEntity.noContent().build();
  }
}
