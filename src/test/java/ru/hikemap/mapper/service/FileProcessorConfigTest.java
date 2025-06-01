package ru.hikemap.mapper.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import ru.hikemap.config.FileProcessorConfig;
import ru.hikemap.service.files.processor.FileProcessor;
import ru.hikemap.service.files.processor.GpxProcessor;

class FileProcessorConfigTest {

  @Test
  void fileProcessorMap_ShouldCreateMapWithProcessors() {
    // Arrange
    FileProcessorConfig config = new FileProcessorConfig();
    FileProcessor gpxProcessor = mock(GpxProcessor.class);
    List<FileProcessor> processors = List.of(gpxProcessor);

    // Act
    Map<String, FileProcessor> result = config.fileProcessorMap(processors);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    assertTrue(result.containsKey("GPX"));
    assertEquals(gpxProcessor, result.get("GPX"));
  }
}
