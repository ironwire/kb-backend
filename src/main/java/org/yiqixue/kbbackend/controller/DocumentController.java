package org.yiqixue.kbbackend.controller;

import org.yiqixue.kbbackend.dto.DocumentDto;
import org.yiqixue.kbbackend.dto.DocumentUploadDto;
import org.yiqixue.kbbackend.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<DocumentDto> uploadDocument(
            @Valid @ModelAttribute DocumentUploadDto uploadDto,
            Authentication authentication) {

        DocumentDto document = documentService.uploadDocument(uploadDto, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(document);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentDto> getDocument(@PathVariable Long id) {
        return documentService.getDocumentById(id)
                .map(doc -> ResponseEntity.ok(doc))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<DocumentDto>> getAllDocuments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<DocumentDto> documents = documentService.getAllDocuments(pageable);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/my-documents")
    public ResponseEntity<List<DocumentDto>> getMyDocuments(Authentication authentication) {
        List<DocumentDto> documents = documentService.getDocumentsByUser(authentication.getName());
        return ResponseEntity.ok(documents);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentDto> updateDocument(
            @PathVariable Long id,
            @Valid @RequestBody DocumentDto documentDto) {

        DocumentDto updated = documentService.updateDocument(id, documentDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}
