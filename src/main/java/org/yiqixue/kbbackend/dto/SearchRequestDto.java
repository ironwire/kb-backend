package org.yiqixue.kbbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequestDto {
    private String query;
    private int page = 0;
    private int size = 10;
    private String sortBy;
    private String sortDirection;
    private String sortOrder;
    private boolean includeContent = false;
}
