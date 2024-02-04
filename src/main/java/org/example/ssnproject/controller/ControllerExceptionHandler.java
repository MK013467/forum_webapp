package org.example.ssnproject.controller;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {


    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException() {
        return "permissionError";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(){
        return "generalErrors";
    }

}
