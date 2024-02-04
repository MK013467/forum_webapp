package org.example.ssnproject.service.PostService;

import org.example.ssnproject.dto.PostDto;
import org.example.ssnproject.entity.PostEntity;

import java.time.LocalDateTime;

//Custom Mapper to map PostEntity to PostDto
public class PostMapper {

    public static PostDto toDto(PostEntity postEntity){
        LocalDateTime displayTime = postEntity.getUpdatedTime() == null ? postEntity.getCreatedTime() : postEntity.getUpdatedTime();
        return PostDto.builder()
                .id(postEntity.getId())
                .title(postEntity.getTitle())
                .writer(postEntity.getWriter().getUsername())
                .contents(postEntity.getContents())
                .views(postEntity.getViews())
                .displayTime(displayTime)
                .build();
    }
}
