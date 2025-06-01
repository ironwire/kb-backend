package org.yiqixue.kbbackend.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.Map;

public interface TikaService {
    String extractText(MultipartFile file);
    String extractText(InputStream inputStream, String filename);
    Map<String, String> extractMetadata(MultipartFile file);
    String detectContentType(MultipartFile file);
    boolean isSupportedFormat(String contentType);
}
