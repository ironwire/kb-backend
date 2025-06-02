package org.yiqixue.kbbackend.repository.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.yiqixue.kbbackend.entity.KnowledgeDocument;

@Repository
public interface KnowledgeDocumentElasticsearchRepository extends ElasticsearchRepository<KnowledgeDocument, Long> {
    // The ElasticsearchRepository interface already provides:
    // - save(T entity)
    // - saveAll(Iterable<T> entities)
    // - findById(ID id)
    // - findAll()
    // - count()
    // - deleteById(ID id)
    // - delete(T entity)
    // - existsById(ID id)
    
    // Add custom query methods here
    // For example:
    // List<KnowledgeDocument> findByTitleContaining(String title);
}