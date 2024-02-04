package org.example.ssnproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.ssnproject.dto.CommentDto;
import org.example.ssnproject.service.PostService.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public ResponseEntity createComment(@ModelAttribute CommentDto commentDto, Principal principal) throws Exception {
        commentDto.setCommentWriter(principal.getName());
        commentService.createComment(commentDto);
        List<CommentDto> commentDtoList = commentService.findAll(commentDto.getPostId());
        return new ResponseEntity<>(commentDtoList, HttpStatus.OK);
    }
}
