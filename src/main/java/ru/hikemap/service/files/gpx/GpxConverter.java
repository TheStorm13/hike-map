package ru.hikemap.service.files.gpx;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import javax.xml.parsers.DocumentBuilderFactory;
import org.geolatte.geom.*;
import org.geolatte.geom.crs.CoordinateReferenceSystem;
import org.geolatte.geom.crs.CoordinateReferenceSystems;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Component
public class GpxConverter {

  public static LineString<G2D> convertGpxToLineString(Path gpxFile)
    throws Exception {
    File file = gpxFile.toFile();

    // Проверка существования файла
    if (!file.exists()) {
      throw new FileNotFoundException("GPX file not found: " + gpxFile);
    }

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true); // Важно для GPX с namespace!
    Document doc = factory.newDocumentBuilder().parse(gpxFile.toString());

    NodeList waypoints = doc.getElementsByTagName("wpt");
    // Создаём CRS (WGS84)
    CoordinateReferenceSystem<G2D> crs = CoordinateReferenceSystems.WGS84;

    // Создаём билдер для последовательности координат
    PositionSequenceBuilder<G2D> sequenceBuilder =
      PositionSequenceBuilders.fixedSized(waypoints.getLength(), G2D.class);

    // Заполняем координаты
    //todo: проверить на правильное добавление и добавление времени
    for (int i = 0; i < waypoints.getLength(); i++) {
      Node wpt = waypoints.item(i);
      NamedNodeMap attrs = wpt.getAttributes();

      double lat = Double.parseDouble(
        attrs.getNamedItem("lat").getTextContent()
      );
      double lon = Double.parseDouble(
        attrs.getNamedItem("lon").getTextContent()
      );

      sequenceBuilder.add(lon, lat); // Geolatte использует (x, y) → (longitude, latitude)
    }

    // Создаём LineString
    PositionSequence<G2D> positions = sequenceBuilder.toPositionSequence();
    LineString<G2D> lineString = new LineString<>(positions, crs);

    return lineString;
  }
}
