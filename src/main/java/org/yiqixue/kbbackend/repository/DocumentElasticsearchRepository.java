package org.yiqixue.kbbackend.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.yiqixue.kbbackend.entity.KnowledgeDocument;

public interface DocumentElasticsearchRepository extends ElasticsearchRepository<KnowledgeDocument, Long> {
}
