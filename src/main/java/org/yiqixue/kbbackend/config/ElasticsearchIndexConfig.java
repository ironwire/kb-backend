package org.yiqixue.kbbackend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;
import org.yiqixue.kbbackend.entity.KnowledgeDocument;

@Component
public class ElasticsearchIndexConfig {

    private final ElasticsearchOperations elasticsearchOperations;

    @Autowired
    public ElasticsearchIndexConfig(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initIndices() {
        // Create index if it doesn't exist
        if (!elasticsearchOperations.indexOps(KnowledgeDocument.class).exists()) {
            elasticsearchOperations.indexOps(KnowledgeDocument.class).create();
        }
    }
}