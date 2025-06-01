package org.yiqixue.kbbackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "documents")
@Document(indexName = "documents")
@Setting(settingPath = "/elasticsearch/document-settings.json")
public class KnowledgeDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Field(type = FieldType.Text)
    private String title;

    @Column(nullable = false)
    @Field(type = FieldType.Keyword)
    private String filename;

    @Column(name = "file_path")
    @Field(type = FieldType.Keyword)
    private String filePath;

    @Column(name = "content_type")
    @Field(type = FieldType.Keyword)
    private String contentType;

    @Column(name = "file_size")
    @Field(type = FieldType.Long)
    private Long fileSize;

    @Lob
    @Column(name = "extracted_content", columnDefinition = "TEXT")
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String extractedContent;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String summary;

    @ElementCollection
    @Field(type = FieldType.Keyword)
    private List<String> tags;

    @Column(name = "created_at")
    @Field(type = FieldType.Date)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Field(type = FieldType.Date)
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    @Field(type = FieldType.Keyword)
    private String createdBy;

    @Field(type = FieldType.Keyword)
    private String status; // PROCESSING, COMPLETED, FAILED

    @OneToOne(mappedBy = "document", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private DocumentMetadata metadata;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
