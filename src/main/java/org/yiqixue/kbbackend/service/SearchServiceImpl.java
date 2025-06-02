package org.yiqixue.kbbackend.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;
import org.yiqixue.kbbackend.dto.DocumentDto;
import org.yiqixue.kbbackend.dto.SearchRequestDto;
import org.yiqixue.kbbackend.dto.SearchResponseDto;
import org.yiqixue.kbbackend.entity.KnowledgeDocument;
import org.yiqixue.kbbackend.repository.elasticsearch.KnowledgeDocumentElasticsearchRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final ElasticsearchOperations elasticsearchOperations;
    private final KnowledgeDocumentElasticsearchRepository elasticsearchRepository;

    @Override
    public SearchResponseDto search(SearchRequestDto request) {
        try {
            // Create a query using Spring Data Elasticsearch
            NativeQuery searchQuery = new NativeQuery(
                Query.of(q -> q
                    .multiMatch(m -> m
                        .query(request.getQuery())
                        .fields("title", "extractedContent")
                    )
                )
            );
            
            // Add pagination
            searchQuery.setPageable(PageRequest.of(request.getPage(), request.getSize()));
            
            // Start timing
            long startTime = System.currentTimeMillis();
            
            // Execute search
            SearchHits<KnowledgeDocument> searchHits = 
                elasticsearchOperations.search(searchQuery, KnowledgeDocument.class);
            
            // Calculate search time
            long searchTime = System.currentTimeMillis() - startTime;
            
            // Extract results and convert to DocumentDto
            List<DocumentDto> results = searchHits.getSearchHits().stream()
                .map(hit -> convertToDto(hit.getContent()))
                .collect(Collectors.toList());
            
            // Create response
            return new SearchResponseDto(
                results,
                searchHits.getTotalHits(),
                request.getPage(),
                request.getSize(),
                searchTime
            );
        } catch (Exception e) {
            log.error("Error executing search", e);
            throw new RuntimeException("Error executing search", e);
        }
    }

    private DocumentDto convertToDto(KnowledgeDocument document) {
        DocumentDto dto = new DocumentDto();
        dto.setId(document.getId());
        dto.setTitle(document.getTitle());
        dto.setFilename(document.getFilename());
        dto.setFileType(document.getFileType());
        dto.setFileSize(document.getFileSize());
        dto.setUploadDate(document.getUploadDate());
        dto.setStatus(document.getStatus());
        
        // Add a snippet of content if available
        if (document.getExtractedContent() != null && !document.getExtractedContent().isEmpty()) {
            int maxLength = 200;
            String content = document.getExtractedContent();
            dto.setContentSnippet(content.length() > maxLength ? 
                content.substring(0, maxLength) + "..." : content);
        }
        
        return dto;
    }

    @Override
    public void indexDocument(KnowledgeDocument document) {
        try {
            log.info("Indexing document: {}", document.getId());
            elasticsearchRepository.save(document);
        } catch (Exception e) {
            log.error("Error indexing document", e);
        }
    }

    @Override
    public void deleteDocument(String documentId) {
        try {
            log.info("Deleting document from index: {}", documentId);
            elasticsearchRepository.deleteById(Long.valueOf(documentId));
        } catch (Exception e) {
            log.error("Error deleting document from index", e);
        }
    }
    
    @Override
    public void reindexAllDocuments() {
        // Implementation depends on how you want to handle reindexing
        // This might involve fetching all documents from your database
        // and saving them to Elasticsearch
    }
}
