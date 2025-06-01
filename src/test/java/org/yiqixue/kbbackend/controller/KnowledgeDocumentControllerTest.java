package org.yiqixue.kbbackend.controller;

import org.yiqixue.kbbackend.dto.DocumentDto;
import org.yiqixue.kbbackend.service.DocumentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DocumentController.class)
class KnowledgeDocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocumentService documentService;

    @Autowired
    private ObjectMapper objectMapper;

    private DocumentDto testDocument;

    @BeforeEach
    void setUp() {
        testDocument = new DocumentDto();
        testDocument.setId(1L);
        testDocument.setTitle("Test Document");
        testDocument.setFilename("test.pdf");
        testDocument.setContentType("application/pdf");
    }

    @Test
    @WithMockUser(username = "testuser")
    void uploadDocument_Success() throws Exception {
        // Given
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.pdf", "application/pdf", "test content".getBytes()
        );
        when(documentService.uploadDocument(any(), eq("testuser"))).thenReturn(testDocument);

        // When & Then
        mockMvc.perform(multipart("/api/documents/upload")
                        .file(file)
                        .param("title", "Test Document")
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Document"));
    }

    @Test
    @WithMockUser
    void getDocument_Found() throws Exception {
        // Given
        when(documentService.getDocumentById(1L)).thenReturn(Optional.of(testDocument));

        // When & Then
        mockMvc.perform(get("/api/documents/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Document"));
    }

    @Test
    @WithMockUser
    void getDocument_NotFound() throws Exception {
        // Given
        when(documentService.getDocumentById(1L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/documents/1"))
                .andExpect(status().isNotFound());
    }
}

