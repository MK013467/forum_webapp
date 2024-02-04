package org.example.ssnproject.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

//Dto used when creating or updating the post
//get data in create_post.html -> PostWriteDto -> PostEntity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostWriteDto {


    @NotNull(message = "Please enter a title")
    private String title;

    @NotNull(message = "Please enter contents")
    @Size( max =500)
    private String contents;

}
