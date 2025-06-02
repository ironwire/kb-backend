package org.yiqixue.kbbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponseDto {
    private List<DocumentDto> results;
    private long totalHits;
    private int page;
    private int size;
    private long searchTimeMs;
}