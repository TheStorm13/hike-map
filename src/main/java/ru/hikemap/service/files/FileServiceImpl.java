package ru.hikemap.service.files;

import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hikemap.repository.hike.HikeRepository;
import ru.hikemap.service.files.processor.FileProcessor;

@Service
public class FileServiceImpl implements FileService {

  private final Map<String, FileProcessor> processors;
  private final HikeRepository hikeRepository; // Для работы с объектом Hike

  public FileServiceImpl(
    @Qualifier("fileProcessorMap") Map<String, FileProcessor> processors,
    HikeRepository hikeRepository
  ) {
    this.processors = processors;
    this.hikeRepository = hikeRepository;
  }

  @Override
  public String saveFile(Long hikeId, MultipartFile file, String fileType) {
    //todo: проверить может у объекта уже есть файл, тогда его нужно удалить

    // Найти обработчик по типу файла
    FileProcessor processor = processors.get(fileType.toUpperCase());

    if (processor == null) {
      throw new IllegalArgumentException(
        "File type is not supported: " + fileType
      );
    }

    String fileName;

    // Передать обработку в стратегию
    try {
      fileName = processor.processFile(hikeId, file);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return fileName;
  }

  @Override
  public Resource loadFile(Long hikeId, String fileType) {
    // Убедимся, что у нас есть обработчик для указанного типа файла
    FileProcessor processor = processors.get(fileType.toUpperCase());

    if (processor == null) {
      throw new IllegalArgumentException("Unsupported file type: " + fileType);
    }

    try {
      Resource resource = processor.loadFile(hikeId);
      if (resource == null || !resource.exists()) {
        throw new IllegalStateException("Failed to load file: " + hikeId);
      }
      return resource;
    } catch (Exception e) {
      throw new RuntimeException("Error loading file: " + hikeId, e);
    }
  }

  @Override
  public void removeFile(Long hikeId, String fileType) {
    FileProcessor processor = processors.get(fileType.toUpperCase());

    if (processor == null) {
      throw new IllegalArgumentException("Unsupported file type: " + fileType);
    }

    processor.removeFile(hikeId);
  }
}
