package org.example.ssnproject.service.PostService;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.ssnproject.dto.PostDto;
import org.example.ssnproject.dto.PostWriteDto;
import org.example.ssnproject.entity.PostEntity;
import org.example.ssnproject.entity.UserEntity;
import org.example.ssnproject.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Getter
@Setter
@Service
public class PostService {

    private final PostRepository postRepository;
    public void createPost(@Valid PostWriteDto postWriteDto, UserEntity userEntity){

        PostEntity postEntity = toPostEntity(postWriteDto , userEntity);
        postRepository.save(postEntity);

    }

    public PostEntity toPostEntity(@Valid PostWriteDto postDto, UserEntity userEntity){

        PostEntity postEntity = PostEntity.builder()
                .title(postDto.getTitle())
                .writer(userEntity)
                .contents(postDto.getContents())
                .views(0)
                .build();
        return postEntity;
    }

    public PostDto toPostDto(PostEntity postEntity){

        LocalDateTime displayTime = postEntity.getUpdatedTime()==null ? postEntity.getCreatedTime():postEntity.getUpdatedTime();


        PostDto postDto = PostDto.builder()
                .id(postEntity.getId())
                .title(postEntity.getTitle())
                .writer(postEntity.getWriter().getUsername())
                .contents(postEntity.getContents())
                .displayTime(displayTime)
                .views(postEntity.getViews())
                .build();

        return postDto;
    }

    @Transactional
    public void updateViews(Long id) {

        postRepository.updateViews(id);
    }

    public PostEntity findById(Long id) throws Exception {
        Optional<PostEntity> _postEntity = postRepository.findById(id);

        if(!_postEntity.isPresent()){
            throw new Exception("Post not found");
        }
        else{


            return _postEntity.get();
        }

    }


    @Transactional
    public void update(Long id, PostWriteDto postWriteDto) throws Exception {


        postRepository.updatePost(id , postWriteDto.getTitle() , postWriteDto.getContents());
    }

    public void delete(Long id) {

        postRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<PostDto> pageList(int page_no , int pageSize ) {

//        int page = pageable.getPageNumber()-1;
//        int pageLimit = 8; //8 posts per page
        Pageable pageable = PageRequest.of(page_no-1 , pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<PostEntity> postPages = postRepository.findAll(pageable);

        return postPages.map(PostMapper::toDto);

    }
}
