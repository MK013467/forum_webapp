package org.example.ssnproject.service;

import jakarta.inject.Inject;
import org.assertj.core.api.Assertions;
import org.example.ssnproject.dto.PostDto;
import org.example.ssnproject.dto.PostWriteDto;
import org.example.ssnproject.entity.PostEntity;
import org.example.ssnproject.entity.UserEntity;
import org.example.ssnproject.repository.PostRepository;
import org.example.ssnproject.repository.UserRepository;
import org.example.ssnproject.service.PostService.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTests {
    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPostTest() {
        PostWriteDto postWriteDto = new PostWriteDto();
        postWriteDto.setTitle("Test Title");
        postWriteDto.setContents("Test Content");

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");

        postService.createPost(postWriteDto, userEntity);

        verify(postRepository).save(any(PostEntity.class));
    }

    @Test
    void updateViewsTest() {
        Long postId = 1L;
        postService.updateViews(postId);

        verify(postRepository).updateViews(postId);
    }

    @Test
    void findByIdTest() throws Exception {
        Long postId = 1L;
        PostEntity postEntity = new PostEntity();
        postEntity.setId(postId);
        when(postRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        PostEntity found = postService.findById(postId);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(postId);
    }


    @Test
    void deleteTest() {
        Long postId = 1L;
        postService.delete(postId);

        verify(postRepository).deleteById(postId);
    }

    @Test
    void pageListTest() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<PostEntity> page = new PageImpl<>(Collections.singletonList(new PostEntity()));
        when(postRepository.findAll(pageable)).thenReturn(page);

        Page<PostDto> resultPage = postService.pageList(1, 10);

        Assertions.assertThat(resultPage).isNotNull();
        verify(postRepository).findAll(pageable);
    }
}
