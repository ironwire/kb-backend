package org.yiqixue.kbbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentUploadDto {

    @NotNull(message = "File cannot be null")
    private MultipartFile file;

    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    private List<String> tags;
}
