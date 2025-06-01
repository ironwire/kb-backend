package org.yiqixue.kbbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponseDto {
    private List<DocumentDto> documents;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int size;
    private long searchTime; // in milliseconds
    private String searchQuery;
}