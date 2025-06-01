package org.yiqixue.kbbackend.repository;
import org.yiqixue.kbbackend.entity.KnowledgeDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDateTime;
import java.util.List;

public interface DocumentRepository extends JpaRepository<KnowledgeDocument,Long> {
    List<KnowledgeDocument> findByCreatedBy(String createdBy);

    List<KnowledgeDocument> findByStatus(String status);

    List<KnowledgeDocument> findByContentType(String contentType);

    @Query("SELECT d FROM KnowledgeDocument d WHERE d.title LIKE %:title%")
    List<KnowledgeDocument> findByTitleContaining(@Param("title") String title);

    @Query("SELECT d FROM KnowledgeDocument d WHERE d.createdAt BETWEEN :startDate AND :endDate")
    List<KnowledgeDocument> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate);

    @Query("SELECT d FROM KnowledgeDocument d JOIN d.tags t WHERE t IN :tags")
    List<KnowledgeDocument> findByTagsIn(@Param("tags") List<String> tags);

    @Query("SELECT COUNT(d) FROM KnowledgeDocument d WHERE d.createdBy = :username")
    long countByCreatedBy(@Param("username") String username);

    @Query("SELECT COUNT(d) FROM KnowledgeDocument d WHERE d.status = :status")
    long countByStatus(@Param("status") String status);
}
