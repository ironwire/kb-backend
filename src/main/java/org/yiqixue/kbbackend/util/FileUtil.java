package org.yiqixue.kbbackend.util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
public class FileUtil {

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
            "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "rtf", "html"
    );

    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB

    public static boolean isValidFileExtension(String filename) {
        String extension = FilenameUtils.getExtension(filename).toLowerCase();
        return ALLOWED_EXTENSIONS.contains(extension);
    }

    public static boolean isValidFileSize(MultipartFile file) {
        return file.getSize() <= MAX_FILE_SIZE;
    }

    public static String generateUniqueFilename(String originalFilename) {
        String extension = FilenameUtils.getExtension(originalFilename);
        String baseName = FilenameUtils.getBaseName(originalFilename);
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        return String.format("%s_%s.%s", baseName, uuid, extension);
    }

    public static String formatFileSize(long size) {
        if (size <= 0) return "0 B";

        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));

        return new DecimalFormat("#,##0.#")
                .format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static void createDirectoryIfNotExists(String directoryPath) {
        try {
            Path path = Paths.get(directoryPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                log.info("Created directory: {}", directoryPath);
            }
        } catch (IOException e) {
            log.error("Failed to create directory: {}", directoryPath, e);
            throw new RuntimeException("Failed to create directory", e);
        }
    }

    public static boolean deleteFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            log.error("Failed to delete file: {}", filePath, e);
            return false;
        }
    }

    public static String getFileExtension(String filename) {
        return FilenameUtils.getExtension(filename).toLowerCase();
    }

    public static boolean isImageFile(String contentType) {
        return contentType != null && contentType.startsWith("image/");
    }

    public static boolean isDocumentFile(String contentType) {
        return contentType != null && (
                contentType.startsWith("application/") ||
                        contentType.startsWith("text/")
        );
    }
}