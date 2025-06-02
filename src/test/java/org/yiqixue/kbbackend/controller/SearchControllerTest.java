package org.yiqixue.kbbackend.controller;

import org.yiqixue.kbbackend.dto.SearchRequestDto;
import org.yiqixue.kbbackend.dto.SearchResponseDto;
import org.yiqixue.kbbackend.service.SearchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SearchController.class)
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchService searchService;

    @Autowired
    private ObjectMapper objectMapper;

    private SearchRequestDto searchRequest;
    private SearchResponseDto searchResponse;

    @BeforeEach
    void setUp() {
        searchRequest = new SearchRequestDto();
        searchRequest.setQuery("test query");
        searchRequest.setPage(0);
        searchRequest.setSize(10);

        searchResponse = new SearchResponseDto();
        searchResponse.setResults(Collections.emptyList());
        searchResponse.setTotalHits(0);
        searchResponse.setPage(0);
        searchResponse.setSize(10);
        searchResponse.setSearchTimeMs(0);
    }

    @Test
    @WithMockUser
    void search_Success() throws Exception {
        // Given
        when(searchService.search(any(SearchRequestDto.class))).thenReturn(searchResponse);

        // When & Then
        mockMvc.perform(post("/api/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalHits").value(0));
    }

    @Test
    @WithMockUser
    void searchByQuery_Success() throws Exception {
        // Given
        when(searchService.search(any(SearchRequestDto.class))).thenReturn(searchResponse);

        // When & Then
        mockMvc.perform(get("/api/search")
                        .param("query", "test query")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalHits").value(0));
    }
}