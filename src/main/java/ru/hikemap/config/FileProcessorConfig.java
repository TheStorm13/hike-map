package ru.hikemap.config;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.hikemap.service.files.processor.FileProcessor;

@Configuration
public class FileProcessorConfig {

  @Bean("fileProcessorMap")
  public Map<String, FileProcessor> fileProcessorMap(
    List<FileProcessor> processors
  ) {
    Map<String, FileProcessor> processorMap = processors
      .stream()
      .collect(
        Collectors.toMap(
          processor ->
            processor
              .getClass()
              .getSimpleName()
              .replace("Processor", "")
              .toUpperCase(),
          processor -> processor
        )
      );

    return processorMap;
  }
}
