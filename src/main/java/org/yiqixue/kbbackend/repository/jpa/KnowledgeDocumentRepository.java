package org.yiqixue.kbbackend.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yiqixue.kbbackend.entity.KnowledgeDocument;

import java.util.List;

@Repository
public interface KnowledgeDocumentRepository extends JpaRepository<KnowledgeDocument, Long> {
    // JPA specific methods
    List<KnowledgeDocument> findByCreatedBy(String username);
}
