package org.example.ssnproject.repository;

import org.assertj.core.api.Assertions;
import org.example.ssnproject.entity.PostEntity;
import org.example.ssnproject.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostRepositoryTests {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private PostEntity postEntity;

    private PostEntity postEntity2;

    private UserEntity userEntity;

    private UserEntity userEntity2;


    @BeforeEach
    public void setUpTestData(){

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
                .title("test post entity")
                .writer(userEntity)
                .contents("contents")
                .build();

        postEntity2 = PostEntity.builder()
                .title("another post ENtity")
                .writer(userEntity2)
                .contents("contents")
                .build();

        //to Test Transactional updateViews method
        userRepository.save(userEntity);
        userRepository.save(userEntity2);
    }

    @Test
    public void PostRepository_SaveAll_ReturnSavedPost(){

        PostEntity savedPostEntity = postRepository.save(postEntity);
        Assertions.assertThat(savedPostEntity).isNotNull();

    }

    @Test
    public void PostRepository_findById_ReturnSaved(){

        PostEntity savedPostEntity = postRepository.save(postEntity);
        PostEntity returned_postEntity = postRepository.findById(postEntity.getId()).get();
        Assertions.assertThat(returned_postEntity).isNotNull();
        Assertions.assertThat(savedPostEntity.getId()).isEqualTo(returned_postEntity.getId());
    }

    @Test
    public void PostRepository_updatePost_ReturnUpdatedPost(){

        PostEntity savedPostEntity = postRepository.save(postEntity);
        String updatedTitle = "Updated Title";
        String updatedContents = "Updated Contents";

        postRepository.updatePost(savedPostEntity.getId(), updatedTitle, updatedContents);
        PostEntity updatedPost = postRepository.findById(savedPostEntity.getId()).get();

        Assertions.assertThat(updatedPost.getTitle()).isEqualTo(updatedTitle);
        Assertions.assertThat(updatedPost.getContents()).isEqualTo(updatedContents);
    }

    @Test
    public void PostRepository_deletePost_ReturnEmptyPost(){
        PostEntity savedPostEntity = postRepository.save(postEntity);
        postRepository.deleteById(savedPostEntity.getId());

        Optional<PostEntity> returedPost =  postRepository.findById(savedPostEntity.getId());

        Assertions.assertThat(returedPost).isEmpty();
    }

    @Test
    public void PostRepository_updateViews_ReturnViewEqualsViewsOnePost(){
        PostEntity savedPostEntity = postRepository.save(postEntity);

        postRepository.updateViews(postEntity.getId());
        PostEntity updatededPost = postRepository.findById(postEntity.getId()).get();

    }
}
