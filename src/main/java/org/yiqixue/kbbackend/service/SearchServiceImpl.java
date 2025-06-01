package org.yiqixue.kbbackend.service;
import org.yiqixue.kbbackend.dto.*;
import org.yiqixue.kbbackend.entity.KnowledgeDocument;
import org.yiqixue.kbbackend.exception.SearchException;
import org.yiqixue.kbbackend.repository.DocumentElasticsearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchServiceImpl implements SearchService {

    private final DocumentElasticsearchRepository documentElasticsearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public SearchResponseDto search(SearchRequestDto request) {
        try {
            long startTime = System.currentTimeMillis();

            // Create pageable with sorting
            Pageable pageable = createPageable(request);

            // For now, use a simple search - can be enhanced later with custom queries
            Page<KnowledgeDocument> documentPage;
            if (request.getQuery() != null && !request.getQuery().trim().isEmpty()) {
                // Simple text search across all documents
                // TODO: Implement proper text search with Chinese analysis
                documentPage = documentElasticsearchRepository.findAll(pageable);
            } else {
                documentPage = documentElasticsearchRepository.findAll(pageable);
            }

            long searchTime = System.currentTimeMillis() - startTime;

            return buildSearchResponse(documentPage, request, searchTime);

        } catch (Exception e) {
            log.error("Error executing search query: {}", request.getQuery(), e);
            throw new SearchException("Failed to execute search", e);
        }
    }

    @Override
    public void indexDocument(KnowledgeDocument document) {
        try {
            documentElasticsearchRepository.save(document);
            log.info("Document indexed successfully: {}", document.getId());

        } catch (Exception e) {
            log.error("Error indexing document: {}", document.getId(), e);
            throw new SearchException("Failed to index document", e);
        }
    }

    @Override
    public void deleteDocument(String documentId) {
        try {
            documentElasticsearchRepository.deleteById(Long.valueOf(documentId));
            log.info("Document deleted from index: {}", documentId);

        } catch (Exception e) {
            log.error("Error deleting document from index: {}", documentId, e);
            throw new SearchException("Failed to delete document from index", e);
        }
    }

    @Override
    public void reindexAllDocuments() {
        log.info("Reindexing all documents...");
        try {
            // Delete all existing documents in the index
            documentElasticsearchRepository.deleteAll();
            log.info("Cleared existing index");

            // TODO: Fetch all documents from the database and reindex them
            // This would require injecting the JPA repository and iterating through all documents
            log.info("Reindexing completed");

        } catch (Exception e) {
            log.error("Error during reindexing", e);
            throw new SearchException("Failed to reindex documents", e);
        }
    }

    private Pageable createPageable(SearchRequestDto request) {
        Sort sort = createSort(request);
        return PageRequest.of(request.getPage(), request.getSize(), sort);
    }

    private Sort createSort(SearchRequestDto request) {
        Sort.Direction direction = "desc".equalsIgnoreCase(request.getSortOrder())
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        switch (request.getSortBy()) {
            case "date":
                return Sort.by(direction, "createdAt");
            case "title":
                return Sort.by(direction, "title");
            default: // relevance
                return Sort.unsorted();
        }
    }

    private SearchResponseDto buildSearchResponse(Page<KnowledgeDocument> documentPage,
                                                  SearchRequestDto request,
                                                  long searchTime) {
        List<DocumentDto> documents = documentPage.getContent()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        SearchResponseDto searchResponse = new SearchResponseDto();
        searchResponse.setDocuments(documents);
        searchResponse.setTotalElements(documentPage.getTotalElements());
        searchResponse.setTotalPages(documentPage.getTotalPages());
        searchResponse.setCurrentPage(request.getPage());
        searchResponse.setSize(request.getSize());
        searchResponse.setSearchTime(searchTime);
        searchResponse.setSearchQuery(request.getQuery());

        return searchResponse;
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

        // Add metadata if available
        if (document.getMetadata() != null && document.getMetadata().getCustomMetadata() != null) {
            dto.setMetadata(document.getMetadata().getCustomMetadata());
        }

        return dto;
    }
}