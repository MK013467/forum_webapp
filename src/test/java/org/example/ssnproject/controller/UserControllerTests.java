package org.example.ssnproject.controller;

import org.example.ssnproject.dto.SignUpForm;
import org.example.ssnproject.service.PostService.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserControllerTests {

    @Mock
    private UserService userService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckSignUpSuccess() {
        SignUpForm signUpForm = SignUpForm.builder()
                .username("testUser")
                .email("test@gmail.com")
                .password1("1234")
                .password2("1234")
                .build();

        when(userService.checkUsernameDuplicate(anyString())).thenReturn(false);
        when(userService.checkEmailDuplicate(anyString())).thenReturn(false);
        when(bindingResult.hasErrors()).thenReturn(false);

        String view = userController.checkSignUp(signUpForm, bindingResult);

        verify(userService, times(1)).createUser(any(SignUpForm.class));
        verify(bindingResult, never()).rejectValue(anyString(), anyString(), anyString());
        assert(view.equals("redirect:/"));
    }

    @Test
    void testCheckSignUpFailureDueToUsernameDuplicate() {

        SignUpForm signUpForm = SignUpForm.builder()
                .username("duplicateUser")
                .email("test@gmail.com")
                .password1("1234")
                .password2("1234")
                .build();
        when(userService.checkUsernameDuplicate(anyString())).thenReturn(true);

        String view = userController.checkSignUp(signUpForm, bindingResult);

        verify(userService, never()).createUser(any(SignUpForm.class));
        verify(bindingResult, times(1)).rejectValue("username", "error.username", "username is in use");
        assert(view.equals("sign_up"));
    }

}
