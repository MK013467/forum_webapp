package org.example.ssnproject.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;
    private String commentWriter;
    private String commentContents;
    private Long postId;
    private LocalDateTime displayTime;
}
