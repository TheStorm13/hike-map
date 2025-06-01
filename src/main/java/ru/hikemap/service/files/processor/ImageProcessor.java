package ru.hikemap.service.files.processor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hikemap.entity.Hike.Hike;
import ru.hikemap.repository.hike.HikeRepository;

@Service
@RequiredArgsConstructor
public class ImageProcessor implements FileProcessor {

  @Value("${file.upload-dir}/images/")
  private String uploadDir;

  private final HikeRepository hikeRepository;

  @Override
  public String processFile(Long hikeId, MultipartFile file) throws Exception {
    // Проверить тип файла
    if (!file.getOriginalFilename().endsWith(".png")) {
      throw new IllegalArgumentException("Invalid image file");
    }

    Hike hike = hikeRepository
      .findById(hikeId)
      .orElseThrow(() ->
        new IllegalArgumentException("Hike with ID " + hikeId + " not found.")
      );
    String fileName = "gpx_" + hikeId + ".gpx";

    // Сохранение файла
    Path path = Paths.get(uploadDir + fileName);
    Files.createDirectories(path.getParent());
    Files.write(path, file.getBytes());

    hike.setPhotoPath(fileName);

    hikeRepository.save(hike);

    return file.getOriginalFilename();
  }

  @Override
  public Resource loadFile(Long hikeId) {
    // Получение объекта Hike из базы данных
    Hike hike = hikeRepository
      .findById(hikeId)
      .orElseThrow(() ->
        new IllegalArgumentException("Hike with ID " + hikeId + " not found.")
      );

    // Получение пути к файлу Image, сохранённого для похода
    String fileName = hike.getPhotoPath();

    if (fileName == null || fileName.isEmpty()) {
      throw new IllegalStateException(
        "No Image file associated with Hike ID: " + hikeId
      );
    }

    // Файл должен находиться в указанной директории
    Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
    if (!Files.exists(filePath)) {
      throw new IllegalStateException("File not found: " + fileName);
    }

    // Возвращаем файл в виде ресурса
    try {
      return new org.springframework.core.io.UrlResource(filePath.toUri());
    } catch (Exception e) {
      throw new RuntimeException("Failed to load file: " + fileName, e);
    }
  }

  @Override
  public void removeFile(Long hikeId) {
    // Получение объекта Hike из базы данных
    Hike hike = hikeRepository
      .findById(hikeId)
      .orElseThrow(() ->
        new IllegalArgumentException("Hike with ID " + hikeId + " not found.")
      );

    // Получаем имя файла, связанного с походом
    String fileName = hike.getPhotoPath();

    if (fileName == null || fileName.isEmpty()) {
      throw new IllegalStateException(
        "No Image file to remove for Hike ID: " + hikeId
      );
    }

    // Удаляем файл из файловой системы
    Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
    try {
      Files.deleteIfExists(filePath);
    } catch (Exception e) {
      throw new RuntimeException("Error deleting file: " + fileName, e);
    }

    // Очищаем информацию о файле у объекта Hike
    hike.setTrackGpxPath(null);
    hikeRepository.save(hike);
  }
}
