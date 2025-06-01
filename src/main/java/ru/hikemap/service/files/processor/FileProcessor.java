package ru.hikemap.service.files.processor;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileProcessor {
  String processFile(Long hikeId, MultipartFile file) throws Exception;

  Resource loadFile(Long hikeId);

  void removeFile(Long hikeId);
}
