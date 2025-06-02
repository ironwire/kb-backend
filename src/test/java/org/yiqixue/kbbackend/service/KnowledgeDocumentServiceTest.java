package org.yiqixue.kbbackend.service;

import org.yiqixue.kbbackend.dto.DocumentDto;
import org.yiqixue.kbbackend.dto.DocumentUploadDto;
import org.yiqixue.kbbackend.entity.KnowledgeDocument;
import org.yiqixue.kbbackend.repository.jpa.KnowledgeDocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KnowledgeDocumentServiceTest {

    @Mock
    private KnowledgeDocumentRepository documentRepository;

    @Mock
    private TikaService tikaService;

    @Mock
    private ChineseNlpService chineseNlpService;

    @Mock
    private SearchService searchService;

    @InjectMocks
    private DocumentServiceImpl documentService;

    private MockMultipartFile testFile;
    private DocumentUploadDto uploadDto;
    private KnowledgeDocument testDocument;

    @BeforeEach
    void setUp() {
        // Set upload directory for testing
        ReflectionTestUtils.setField(documentService, "uploadDir", "./test-uploads");

        testFile = new MockMultipartFile(
                "file",
                "test-document.pdf",
                "application/pdf",
                "Test content".getBytes()
        );

        uploadDto = new DocumentUploadDto();
        uploadDto.setFile(testFile);
        uploadDto.setTitle("Test Document");

        testDocument = new KnowledgeDocument();
        testDocument.setId(1L);
        testDocument.setTitle("Test Document");
        testDocument.setFilename("test-document.pdf");
        testDocument.setContentType("application/pdf");
        testDocument.setCreatedBy("testuser");
    }

    @Test
    void uploadDocument_Success() {
        // Given
        when(tikaService.detectContentType(any())).thenReturn("application/pdf");
        when(tikaService.isSupportedFormat(any())).thenReturn(true);
        when(documentRepository.save(any(KnowledgeDocument.class))).thenReturn(testDocument);

        // When
        DocumentDto result = documentService.uploadDocument(uploadDto, "testuser");

        // Then
        assertNotNull(result);
        assertEquals("Test Document", result.getTitle());
        assertEquals("test-document.pdf", result.getFilename());
        verify(documentRepository).save(any(KnowledgeDocument.class));
    }

    @Test
    void getDocumentById_Found() {
        // Given
        when(documentRepository.findById(1L)).thenReturn(Optional.of(testDocument));

        // When
        Optional<DocumentDto> result = documentService.getDocumentById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Test Document", result.get().getTitle());
    }

    @Test
    void getDocumentById_NotFound() {
        // Given
        when(documentRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<DocumentDto> result = documentService.getDocumentById(1L);

        // Then
        assertFalse(result.isPresent());
    }
}
