package org.yiqixue.kbbackend.service;

import org.yiqixue.kbbackend.exception.DocumentProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tika.detect.Detector;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class TikaServiceImpl implements TikaService {

    private final Tika tika;
    private final Parser parser;
    private final Detector detector;

    private static final Set<String> SUPPORTED_TYPES = new HashSet<>(Arrays.asList(
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "application/vnd.ms-powerpoint",
            "application/vnd.openxmlformats-officedocument.presentationml.presentation",
            "text/plain",
            "text/html",
            "application/rtf"
    ));

    @Override
    public String extractText(MultipartFile file) {
        try {
            return extractText(file.getInputStream(), file.getOriginalFilename());
        } catch (IOException e) {
            log.error("Error extracting text from file: {}", file.getOriginalFilename(), e);
            throw new DocumentProcessingException("Failed to extract text from document", e);
        }
    }

    @Override
    public String extractText(InputStream inputStream, String filename) {
        try {
            BodyContentHandler handler = new BodyContentHandler(-1); // No limit
            Metadata metadata = new Metadata();
            ParseContext context = new ParseContext();

            parser.parse(inputStream, handler, metadata, context);
            return handler.toString();
        } catch (IOException | SAXException | TikaException e) {
            log.error("Error extracting text from file: {}", filename, e);
            throw new DocumentProcessingException("Failed to extract text from document: " + filename, e);
        }
    }

    @Override
    public Map<String, String> extractMetadata(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            Metadata metadata = new Metadata();
            BodyContentHandler handler = new BodyContentHandler();
            ParseContext context = new ParseContext();

            parser.parse(inputStream, handler, metadata, context);

            Map<String, String> metadataMap = new HashMap<>();
            for (String name : metadata.names()) {
                metadataMap.put(name, metadata.get(name));
            }
            return metadataMap;
        } catch (IOException | SAXException | TikaException e) {
            log.error("Error extracting metadata from file: {}", file.getOriginalFilename(), e);
            throw new DocumentProcessingException("Failed to extract metadata from document", e);
        }
    }

    @Override
    public String detectContentType(MultipartFile file) {
        try {
            return tika.detect(file.getInputStream(), file.getOriginalFilename());
        } catch (IOException e) {
            log.error("Error detecting content type for file: {}", file.getOriginalFilename(), e);
            throw new DocumentProcessingException("Failed to detect content type", e);
        }
    }

    @Override
    public boolean isSupportedFormat(String contentType) {
        return SUPPORTED_TYPES.contains(contentType);
    }
}

