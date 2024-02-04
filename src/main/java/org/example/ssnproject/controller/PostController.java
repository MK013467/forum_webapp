package org.example.ssnproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ssnproject.dto.CommentDto;
import org.example.ssnproject.dto.PostDto;
import org.example.ssnproject.dto.PostWriteDto;
import org.example.ssnproject.entity.PostEntity;
import org.example.ssnproject.entity.UserEntity;
import org.example.ssnproject.service.PostService.CommentService;
import org.example.ssnproject.service.PostService.PostService;
import org.example.ssnproject.service.PostService.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String createPost(PostWriteDto postWriteDto){

        return "create_post";
    }


//  Using preauthorize annotation to ensure that Only signined user can write a post
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String validatePost(@Valid PostWriteDto postWriteDto, BindingResult bindingResult, Principal principal){
        if(bindingResult.hasErrors()){
            return "create_post";
        }

        UserEntity userEntity = userService.findUser(principal.getName());
        postService.createPost(postWriteDto, userEntity);
        return "redirect:/post/list";
    }


    @GetMapping("/details/{id}")
    public String readPost(@PathVariable Long id , Model model) throws Exception {
            //first get the post and add it to the model.
            PostEntity postEntity = postService.findById(id);
            PostDto postDto = postService.toPostDto(postEntity);
            List<CommentDto> commentList= commentService.findAll(id);
            model.addAttribute("commentList" , commentList);
            model.addAttribute("post", postDto);


            //then update the views
            postService.updateViews(id);

            return "details";
    }

      //display Posts

     @GetMapping("/list/{page_no}")
     public String showPostList(@PathVariable int page_no
             , Model model){

          int pageSize = 8;
          Page<PostDto> page = postService.pageList(page_no , pageSize);

          int pageLimit = 5;
          int startPage = (((int) Math.ceil(((float) page_no / pageLimit))) - 1) * pageLimit + 1;
          int endPage = Math.min(page.getTotalPages(), startPage+pageLimit -1);

          model.addAttribute("posts" ,page);
          model.addAttribute("currentPage", page_no);
          model.addAttribute("startPage", startPage);
          model.addAttribute("endPage", endPage);
          model.addAttribute("previous", page.hasPrevious() );
          model.addAttribute("next", page.hasNext() );


         return "postList";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/update/{id}")
    public String updatePost(@PathVariable("id") Long id, Principal principal,
                             PostWriteDto postWriteDto, Model model) throws Exception {
        PostEntity post = postService.findById(id);
        postWriteDto.setTitle(post.getTitle());
        postWriteDto.setContents(post.getContents());

        model.addAttribute("id" , id);
        return "update_post";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update/{id}")
    public String checkUpdatePost(@PathVariable Long id , Principal principal,
                                  @Valid PostWriteDto postWriteDto , BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()){
            return "update_post";
        }

        PostEntity post = postService.findById(id);
        //check if writer equals signined user.
        if (!post.getWriter().getUsername().equals(principal.getName()) ) {
            throw new AccessDeniedException("You can only update your post");
        }

        postService.update(id, postWriteDto);
        return "redirect:/post/details/" + Long.toString(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable Long id, Principal principal)  {

        //check the current user and the wrtier of the post is same.

        //Get post -> get writer -> check writer's username == principal.name()

        try{
            UserEntity user =  postService.findById(id).getWriter();
            if (!user.getUsername().equals(principal.getName()) ){
                System.out.println("username does not mach:"+user.getUsername());
                throw new AccessDeniedException("You can only delete your post");
            }
        } catch (Exception e) {
            throw new RuntimeException("can not find the post!");
        }
        postService.delete(id);

        return "redirect:/";
    }
}
