package org.yiqixue.kbbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yiqixue.kbbackend.repository.elasticsearch.KnowledgeDocumentElasticsearchRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class ElasticsearchService {

    private final KnowledgeDocumentElasticsearchRepository elasticsearchRepository;

    public boolean isElasticsearchUp() {
        try {
            // Just try to count documents - if it works, Elasticsearch is up
            elasticsearchRepository.count();
            return true;
        } catch (Exception e) {
            log.error("Error checking Elasticsearch status", e);
            return false;
        }
    }
}
