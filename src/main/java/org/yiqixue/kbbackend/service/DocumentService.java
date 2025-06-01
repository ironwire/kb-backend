package org.yiqixue.kbbackend.service;

import org.yiqixue.kbbackend.dto.DocumentDto;
import org.yiqixue.kbbackend.dto.DocumentUploadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DocumentService {
    DocumentDto uploadDocument(DocumentUploadDto uploadDto, String username);
    Optional<DocumentDto> getDocumentById(Long id);
    Page<DocumentDto> getAllDocuments(Pageable pageable);
    List<DocumentDto> getDocumentsByUser(String username);
    void deleteDocument(Long id);
    DocumentDto updateDocument(Long id, DocumentDto documentDto);
    void processDocumentAsync(Long documentId);
}