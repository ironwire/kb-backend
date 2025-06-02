package org.yiqixue.kbbackend.repository.elasticsearch;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.RefreshPolicy;
import org.springframework.stereotype.Repository;
import org.yiqixue.kbbackend.entity.KnowledgeDocument;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class MockKnowledgeDocumentElasticsearchRepository implements KnowledgeDocumentElasticsearchRepository {
    
    private final List<KnowledgeDocument> documents = new ArrayList<>();
    
    @Override
    public <S extends KnowledgeDocument> S save(S entity) {
        // Mock implementation
        documents.add(entity);
        return entity;
    }
    
    @Override
    public <S extends KnowledgeDocument> S save(S entity, RefreshPolicy refreshPolicy) {
        // Mock implementation - just delegate to the regular save method
        return save(entity);
    }
    
    @Override
    public <S extends KnowledgeDocument> Iterable<S> saveAll(Iterable<S> entities) {
        // Mock implementation
        entities.forEach(documents::add);
        return entities;
    }
    
    @Override
    public <S extends KnowledgeDocument> Iterable<S> saveAll(Iterable<S> entities, RefreshPolicy refreshPolicy) {
        // Mock implementation - just delegate to the regular saveAll method
        return saveAll(entities);
    }
    
    @Override
    public Optional<KnowledgeDocument> findById(Long id) {
        return documents.stream()
            .filter(doc -> doc.getId().equals(id))
            .findFirst();
    }
    
    @Override
    public boolean existsById(Long id) {
        return documents.stream().anyMatch(doc -> doc.getId().equals(id));
    }
    
    @Override
    public Iterable<KnowledgeDocument> findAll() {
        return new ArrayList<>(documents);
    }
    
    @Override
    public Iterable<KnowledgeDocument> findAllById(Iterable<Long> ids) {
        List<KnowledgeDocument> result = new ArrayList<>();
        for (Long id : ids) {
            documents.stream()
                .filter(doc -> doc.getId().equals(id))
                .findFirst()
                .ifPresent(result::add);
        }
        return result;
    }
    
    @Override
    public long count() {
        return documents.size();
    }
    
    @Override
    public void deleteById(Long id) {
        documents.removeIf(doc -> doc.getId().equals(id));
    }
    
    @Override
    public void deleteById(Long id, RefreshPolicy refreshPolicy) {
        // Mock implementation - just delegate to the regular deleteById method
        deleteById(id);
    }
    
    @Override
    public void delete(KnowledgeDocument entity) {
        documents.removeIf(doc -> doc.getId().equals(entity.getId()));
    }
    
    @Override
    public void delete(KnowledgeDocument entity, RefreshPolicy refreshPolicy) {
        // Mock implementation - just delegate to the regular delete method
        delete(entity);
    }
    
    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        for (Long id : ids) {
            documents.removeIf(doc -> doc.getId().equals(id));
        }
    }
    
    @Override
    public void deleteAllById(Iterable<? extends Long> ids, RefreshPolicy refreshPolicy) {
        // Mock implementation - just delegate to the regular deleteAllById method
        deleteAllById(ids);
    }
    
    @Override
    public void deleteAll(Iterable<? extends KnowledgeDocument> entities) {
        for (KnowledgeDocument entity : entities) {
            documents.removeIf(doc -> doc.getId().equals(entity.getId()));
        }
    }
    
    @Override
    public void deleteAll(Iterable<? extends KnowledgeDocument> entities, RefreshPolicy refreshPolicy) {
        // Mock implementation - just delegate to the regular deleteAll method
        deleteAll(entities);
    }
    
    @Override
    public void deleteAll() {
        documents.clear();
    }
    
    @Override
    public void deleteAll(RefreshPolicy refreshPolicy) {
        // Mock implementation - just clear the documents list
        documents.clear();
    }
    
    @Override
    public Iterable<KnowledgeDocument> findAll(Sort sort) {
        // Mock implementation - ignoring sort for now
        return new ArrayList<>(documents);
    }
    
    @Override
    public Page<KnowledgeDocument> findAll(Pageable pageable) {
        // Mock implementation - ignoring pagination for now
        return Page.empty();
    }
    
    @Override
    public Page<KnowledgeDocument> searchSimilar(KnowledgeDocument entity, String[] fields, Pageable pageable) {
        // Mock implementation - just return an empty page
        return new PageImpl<>(new ArrayList<>(), pageable, 0);
    }
}