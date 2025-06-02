package org.yiqixue.kbbackend.controller;

import org.yiqixue.kbbackend.dto.*;
import org.yiqixue.kbbackend.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3000"}, allowCredentials = "true")
public class SearchController {

    private final SearchService searchService;

    @PostMapping
    public ResponseEntity<SearchResponseDto> search(@Valid @RequestBody SearchRequestDto request) {
        SearchResponseDto response = searchService.search(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<SearchResponseDto> searchByQuery(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "relevance") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder,
            @RequestParam(defaultValue = "false") boolean includeContent) {

        SearchRequestDto request = new SearchRequestDto();
        request.setQuery(query);
        request.setPage(page);
        request.setSize(size);
        request.setSortBy(sortBy);
        request.setSortOrder(sortOrder);
        request.setIncludeContent(includeContent);

        SearchResponseDto response = searchService.search(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reindex")
    public ResponseEntity<String> reindexDocuments() {
        searchService.reindexAllDocuments();
        return ResponseEntity.ok("Reindexing started");
    }
}
