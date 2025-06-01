package org.yiqixue.kbbackend.service;

import org.yiqixue.kbbackend.dto.SearchRequestDto;
import org.yiqixue.kbbackend.dto.SearchResponseDto;
import org.yiqixue.kbbackend.entity.KnowledgeDocument;

public interface SearchService {
    SearchResponseDto search(SearchRequestDto request);
    void indexDocument(KnowledgeDocument document);
    void deleteDocument(String documentId);
    void reindexAllDocuments();
}
