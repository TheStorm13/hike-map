package ru.hikemap.dto.response;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HikeGeoJsonResponse {

  private String type;
  private List<Map<String, Object>> features;
}
