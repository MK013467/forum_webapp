package org.example.ssnproject.repository;


import org.assertj.core.api.Assertions;
import org.example.ssnproject.entity.CommentEntity;
import org.example.ssnproject.entity.PostEntity;
import org.example.ssnproject.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommentRepositoryTests {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private PostEntity postEntity;

    private PostEntity postEntity2;

    private PostEntity postEntity3;
    private UserEntity userEntity;

    private UserEntity userEntity2;

    private CommentEntity commentEntity;

    private CommentEntity commentEntity2;

    private CommentEntity commentEntity3;


    @BeforeEach
    void setUpTestData() {
        userEntity = UserEntity
                .builder()
                .username("tester")
                .password("1234")
                .email("tester@gmail.com")
                .build();

        userEntity2 = UserEntity
                .builder()
                .username("tester2")
                .password("1234")
                .email("tester2@gmain.com")
                .build();


        postEntity = PostEntity.builder()
                .title("test postEntity")
                .writer(userEntity)
                .contents("contents")
                .build();

        postEntity2 = PostEntity.builder()
                .title("another postEntity")
                .writer(userEntity2)
                .contents("contents")
                .build();

        postEntity3 = PostEntity.builder()
                .title("third postEntity")
                .writer(userEntity)
                .contents("contents")
                .build();

        commentEntity = CommentEntity
                .builder()
                .commentWriter(userEntity)
                .commentContents("bullahhh")
                .postEntity(postEntity)
                .build();

        commentEntity2 = CommentEntity.builder()
                .commentContents("second comment")
                .commentWriter(userEntity)
                .postEntity(postEntity)
                .build();

        commentEntity3 = CommentEntity.builder()
                .commentWriter(userEntity2)
                .commentContents("third comment")
                .postEntity(postEntity2)
                .build();
        userRepository.save(userEntity);
        userRepository.save(userEntity2);
        postRepository.save(postEntity);
        postRepository.save(postEntity2);
        postRepository.save(postEntity3);
        commentRepository.save(commentEntity);
        commentRepository.save(commentEntity2);
        commentRepository.save(commentEntity3);
    }

    @Test
    public void CommentRepository_findAllByPostEntity_ReturnComments(){

        List<CommentEntity> comments = commentRepository.findAllByPostEntityOrderByIdDesc(postEntity);
        List<CommentEntity> comments2 = commentRepository.findAllByPostEntityOrderByIdDesc(postEntity2);
        List<CommentEntity> comments3 = commentRepository.findAllByPostEntityOrderByIdDesc(postEntity3);

        Assertions.assertThat(comments.size()).isEqualTo(2);
        Assertions.assertThat(comments2.size()).isEqualTo(1);
        Assertions.assertThat(comments3).isEmpty();

    }

}



