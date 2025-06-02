package org.yiqixue.kbbackend.service;

import org.yiqixue.kbbackend.dto.DocumentDto;
import org.yiqixue.kbbackend.dto.DocumentUploadDto;
import org.yiqixue.kbbackend.entity.KnowledgeDocument;
import org.yiqixue.kbbackend.entity.DocumentMetadata;
import org.yiqixue.kbbackend.exception.DocumentNotFoundException;
import org.yiqixue.kbbackend.exception.DocumentProcessingException;
import org.yiqixue.kbbackend.repository.jpa.KnowledgeDocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DocumentServiceImpl implements DocumentService {

    private final KnowledgeDocumentRepository documentRepository;
    private final TikaService tikaService;
    private final ChineseNlpService chineseNlpService;
    private final SearchService searchService;

    @Value("${app.file.upload-dir:./uploads}")
    private String uploadDir;

    @Override
    public DocumentDto uploadDocument(DocumentUploadDto uploadDto, String username) {
        MultipartFile file = uploadDto.getFile();

        // Validate file
        if (file.isEmpty()) {
            throw new DocumentProcessingException("File is empty");
        }

        String contentType = tikaService.detectContentType(file);
        if (!tikaService.isSupportedFormat(contentType)) {
            throw new DocumentProcessingException("Unsupported file format: " + contentType);
        }

        // Save file to disk
        String filePath = saveFile(file);

        // Create document entity
        KnowledgeDocument document = new KnowledgeDocument();
        document.setTitle(uploadDto.getTitle() != null ? uploadDto.getTitle() : file.getOriginalFilename());
        document.setFilename(file.getOriginalFilename());
        document.setFilePath(filePath);
        document.setContentType(contentType);
        document.setFileSize(file.getSize());
        document.setCreatedBy(username);
        document.setStatus("PROCESSING");
        document.setTags(uploadDto.getTags());

        document = documentRepository.save(document);

        // Process document asynchronously
        processDocumentAsync(document.getId());

        return convertToDto(document);
    }

    @Async("documentProcessingExecutor")
    @Override
    public void processDocumentAsync(Long documentId) {
        try {
            Optional<KnowledgeDocument> optionalDocument = documentRepository.findById(documentId);
            if (optionalDocument.isEmpty()) {
                log.error("Document not found for processing: {}", documentId);
                return;
            }

            KnowledgeDocument document = optionalDocument.get();
            log.info("Processing document: {}", document.getFilename());

            // Extract text content
            Path filePath = Paths.get(document.getFilePath());
            String extractedText = tikaService.extractText(Files.newInputStream(filePath), document.getFilename());

            document.setExtractedContent(extractedText);

            // Generate summary if content is Chinese
            if (chineseNlpService.isChineseText(extractedText)) {
                String summary = chineseNlpService.generateSummary(extractedText, 200);
                document.setSummary(summary);
            }

            // Extract and save metadata
            DocumentMetadata metadata = new DocumentMetadata();
            metadata.setDocument(document);
            metadata.setLanguage(chineseNlpService.isChineseText(extractedText) ? "zh" : "en");
            metadata.setWordCount(extractedText.split("\\s+").length);
            metadata.setCharacterCount(extractedText.length());

            document.setMetadata(metadata);
            document.setStatus("COMPLETED");

            documentRepository.save(document);

            // Index in Elasticsearch
            searchService.indexDocument(document);

            log.info("Document processing completed: {}", document.getFilename());

        } catch (Exception e) {
            log.error("Error processing document: {}", documentId, e);
            updateDocumentStatus(documentId, "FAILED");
        }
    }

    @Override
    public Optional<DocumentDto> getDocumentById(Long id) {
        return documentRepository.findById(id).map(this::convertToDto);
    }

    @Override
    public Page<DocumentDto> getAllDocuments(Pageable pageable) {
        return documentRepository.findAll(pageable).map(this::convertToDto);
    }

    @Override
    public List<DocumentDto> getDocumentsByUser(String username) {
        return documentRepository.findByCreatedBy(username)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteDocument(Long id) {
        KnowledgeDocument document = documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found: " + id));

        // Delete physical file
        try {
            Files.deleteIfExists(Paths.get(document.getFilePath()));
        } catch (IOException e) {
            log.warn("Could not delete physical file: {}", document.getFilePath(), e);
        }

        // Remove from search index
        searchService.deleteDocument(id.toString());

        // Delete from database
        documentRepository.delete(document);
    }

    @Override
    public DocumentDto updateDocument(Long id, DocumentDto documentDto) {
        KnowledgeDocument document = documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found: " + id));

        document.setTitle(documentDto.getTitle());
        document.setTags(documentDto.getTags());
        document.setSummary(documentDto.getSummary());

        document = documentRepository.save(document);

        // Update search index
        searchService.indexDocument(document);

        return convertToDto(document);
    }

    private String saveFile(MultipartFile file) {
        try {
            String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath);

            return filePath.toString();
        } catch (IOException e) {
            throw new DocumentProcessingException("Failed to save file", e);
        }
    }

    private void updateDocumentStatus(Long documentId, String status) {
        documentRepository.findById(documentId).ifPresent(doc -> {
            doc.setStatus(status);
            documentRepository.save(doc);
        });
    }

    private DocumentDto convertToDto(KnowledgeDocument document) {
        DocumentDto dto = new DocumentDto();
        dto.setId(document.getId());
        dto.setTitle(document.getTitle());
        dto.setFilename(document.getFilename());
        dto.setContentType(document.getContentType());
        dto.setFileSize(document.getFileSize());
        dto.setExtractedContent(document.getExtractedContent());
        dto.setSummary(document.getSummary());
        dto.setTags(document.getTags());
        dto.setCreatedAt(document.getCreatedAt());
        dto.setUpdatedAt(document.getUpdatedAt());
        dto.setCreatedBy(document.getCreatedBy());
        dto.setStatus(document.getStatus());

        if (document.getMetadata() != null) {
            // Create a map with the metadata fields
            Map<String, String> metadataMap = new HashMap<>();
            DocumentMetadata metadata = document.getMetadata();
            metadataMap.put("language", metadata.getLanguage());
            metadataMap.put("wordCount", String.valueOf(metadata.getWordCount()));
            metadataMap.put("characterCount", String.valueOf(metadata.getCharacterCount()));
            
            dto.setMetadata(metadataMap);
        }

        return dto;
    }
}
