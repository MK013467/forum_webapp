package org.example.ssnproject.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.apache.catalina.User;
import org.example.ssnproject.dto.SignUpForm;
import org.example.ssnproject.dto.UserDto;
import org.example.ssnproject.repository.UserRepository;
import org.example.ssnproject.service.PostService.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.naming.Binding;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @GetMapping("/signup")
    public String createUser(SignUpForm signUpForm){

        return "sign_up";
    }

    @PostMapping("/signup")
    public String checkSignUp(@ModelAttribute @Valid SignUpForm signUpForm, BindingResult bindingResult) {

        if (userService.checkUsernameDuplicate(signUpForm.getUsername())) {
            bindingResult.rejectValue("username" , "error.username" , "username is in use");
            return "sign_up";
        }
        if (userService.checkEmailDuplicate(signUpForm.getEmail()) ) {
            bindingResult.rejectValue("email" , "error.email" , "email is in use");
            return "sign_up";
        }

        //check password matches

        if(!signUpForm.getPassword1().equals(signUpForm.getPassword2())){
            bindingResult.rejectValue("password1" , "error.password1" , "two passwords do not match");
            return "sign_up";
        }

        // Proceed with user creation
        try {
            userService.createUser(signUpForm);
            return "redirect:/"; // Redirect to prevent double submission
        }
        catch (Exception e){
            bindingResult.reject("error" , "Sign up failed");

            return "sign_up";

        }
    }

    @GetMapping("/signin")
    public String signIn(UserDto userDto){


        return "sign_in";
    }

    @GetMapping("/signout")
    public String signOut(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }

}
