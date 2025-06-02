package org.yiqixue.kbbackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Document;

@Entity
@Table(name = "document_metadata")
@Data
@Document(indexName = "document_metadata")
public class DocumentMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.annotation.Id
    private Long id;

    @OneToOne
    @JoinColumn(name = "document_id")
    @Field(type = FieldType.Object)
    @org.springframework.data.annotation.Transient
    private KnowledgeDocument document;

    private String language;
    private Integer wordCount;
    private Integer characterCount;
    
    // Add any other metadata fields here
}
