package org.yiqixue.kbbackend.dto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequestDto {

    @NotBlank(message = "Query cannot be empty")
    private String query;

    @Min(value = 0, message = "Page number must be non-negative")
    private int page = 0;

    @Min(value = 1, message = "Size must be at least 1")
    @Max(value = 100, message = "Size cannot exceed 100")
    private int size = 10;

    private List<String> contentTypes;
    private List<String> tags;
    private String sortBy = "relevance"; // relevance, date, title
    private String sortOrder = "desc"; // asc, desc
    private boolean includeContent = false;
}