package ru.hikemap.service.hike.files;

import org.geolatte.geom.LineString;
import org.springframework.core.io.Resource;

public class GpxService {

  public static Resource downloadGpxFile(Long hikeId) {
    //todo: add logic to give the gpx file
    return null;
  }

  public static Resource uploadGpxFile(Long hikeId) {
    //todo: add logic to load the gpx file
    return null;
  }

  public static LineString convertGpxToLineString(Long hikeId) {
    //todo: add logic to convert gpx file to LineString for load to database
    return null;
  }
}
