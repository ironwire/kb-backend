package org.yiqixue.kbbackend.service;

import org.apache.tika.Tika;
import org.apache.tika.detect.Detector;
import org.apache.tika.parser.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TikaServiceTest {

    @Mock
    private Tika tika;

    @Mock
    private Parser parser;

    @Mock
    private Detector detector;

    @InjectMocks
    private TikaServiceImpl tikaService;

    private MockMultipartFile testFile;

    @BeforeEach
    void setUp() {
        testFile = new MockMultipartFile(
                "file",
                "test.pdf",
                "application/pdf",
                "Test content".getBytes()
        );
    }

    @Test
    void detectContentType_Success() throws Exception {
        // Given
        when(tika.detect((InputStream) any(), any(String.class))).thenReturn("application/pdf");

        // When
        String contentType = tikaService.detectContentType(testFile);

        // Then
        assertEquals("application/pdf", contentType);
        verify(tika).detect((InputStream) any(), any(String.class));
    }

    @Test
    void isSupportedFormat_Pdf_ReturnsTrue() {
        // When
        boolean isSupported = tikaService.isSupportedFormat("application/pdf");

        // Then
        assertTrue(isSupported);
    }

    @Test
    void isSupportedFormat_UnsupportedType_ReturnsFalse() {
        // When
        boolean isSupported = tikaService.isSupportedFormat("image/jpeg");

        // Then
        assertFalse(isSupported);
    }
}
