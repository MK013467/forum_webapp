package org.example.ssnproject.service.PostService;

import lombok.RequiredArgsConstructor;
import org.example.ssnproject.dto.CommentDto;
import org.example.ssnproject.entity.CommentEntity;
import org.example.ssnproject.entity.PostEntity;
import org.example.ssnproject.entity.UserEntity;
import org.example.ssnproject.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostService postService;

    public void createComment(CommentDto commentDto) throws Exception {
        try{
            PostEntity postEntity =  postService.findById(commentDto.getPostId());
            UserEntity commentWriterEntity = userService.findUser(commentDto.getCommentWriter());

            CommentEntity commentEntity = CommentEntity.builder()
                    .commentContents(commentDto.getCommentContents())
                    .commentWriter(commentWriterEntity)
                    .postEntity(postEntity)
                    .build();
            commentRepository.save(commentEntity);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static CommentDto  toCommentDto(CommentEntity commentEntity){

        LocalDateTime displayTime = commentEntity.getUpdatedTime()==null ? commentEntity.getCreatedTime() : commentEntity.getUpdatedTime();
        CommentDto commentDto = CommentDto.builder()
                .id(commentEntity.getId())
                .commentContents(commentEntity.getCommentContents())
                .commentWriter(commentEntity.getCommentWriter().getUsername())
                .postId(commentEntity.getPostEntity().getId())
                .displayTime(displayTime)
                .build();

        return commentDto;
    }

//    return all comments in the post
    public List<CommentDto> findAll(Long postId) throws Exception {
        PostEntity postEntity = postService.findById(postId);
        List<CommentEntity> commentEntityList = commentRepository.findAllByPostEntityOrderByIdDesc(postEntity);

        List<CommentDto> commentDtoList = new ArrayList<>();
        for(CommentEntity commentEntity : commentEntityList){
            commentDtoList.add(toCommentDto(commentEntity));
        }

        return commentDtoList;
    }
}
