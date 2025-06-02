package org.yiqixue.kbbackend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yiqixue.kbbackend.dto.SearchRequestDto;
import org.yiqixue.kbbackend.dto.SearchResponseDto;
import org.yiqixue.kbbackend.service.SearchService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
@Slf4j
public class ElasticsearchController {

    private final SearchService searchService;

    @PostMapping
    public ResponseEntity<SearchResponseDto> search(@RequestBody SearchRequestDto request) {
        log.info("Received search request: {}", request);
        SearchResponseDto response = searchService.search(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/reindex")
    public ResponseEntity<Map<String, String>> reindexAll() {
        log.info("Received request to reindex all documents");
        searchService.reindexAllDocuments();
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Reindexing started");
        
        return ResponseEntity.ok(response);
    }
}