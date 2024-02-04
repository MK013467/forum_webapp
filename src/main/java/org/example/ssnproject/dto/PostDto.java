package org.example.ssnproject.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

//Dto for displaying the post
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

//Get data from PostRepository: PostEntity -> PostDto -> display
public class PostDto {

    @NotNull
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String writer;

    private String contents;

    private Integer views;

    private LocalDateTime displayTime;
}
