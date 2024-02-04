package org.example.ssnproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.ssnproject.entity.PostEntity;
import org.example.ssnproject.repository.PostRepository;
import org.example.ssnproject.service.PostService.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

//showing post list at home

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PostService postService;

    @RequestMapping("/")
    public String showHomePage(){

        return "redirect:/post/list/1";
    }

}
