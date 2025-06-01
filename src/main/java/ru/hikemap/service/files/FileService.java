package ru.hikemap.service.files;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileService {
  String saveFile(Long hikeId, MultipartFile file, String fileType);

  Resource loadFile(Long hikeId, String fileType);

  void removeFile(Long hikeId, String fileType);
}
