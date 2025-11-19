package com.realestate.platform.exception;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

// --- THIS IS THE FIX ---
// We remove (basePackages = ...) to make this a truly global handler
// for any @Controller that isn't a @RestController.
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleResourceNotFound(ResourceNotFoundException ex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", ex.getMessage());
        mav.setViewName("error-404");
        return mav;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDenied(AccessDeniedException ex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", "You are not authorized to access this page.");
        mav.setViewName("error-403");
        return mav;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllOtherErrors(Exception ex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", "An unexpected error occurred. Please try again later.");
        System.err.println(ex.getMessage()); 
        mav.setViewName("error-500");
        return mav;
    }
}