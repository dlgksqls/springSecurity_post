package spring.securitystudy.user.advice;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import spring.securitystudy.exception.user.TokenExpiredException;
import spring.securitystudy.exception.user.UserNotFoundException;
import spring.securitystudy.user.exception.UserAlreadyExistsException;

@ControllerAdvice(basePackages = "spring.securitystudy.user.controller")
public class UserControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public String UserNotFoundException(UserNotFoundException ex, Model model){
        model.addAttribute("errorMessage", ex.getMessage());
        return "user/error";
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public String UserAlreadyExistsException(UserAlreadyExistsException ex, Model model){
        model.addAttribute("errorMessage", ex.getMessage());
        return "user/error";
    }

    @ExceptionHandler(TokenExpiredException.class)
    public String TokenExpiredException(TokenExpiredException ex, Model model){
        model.addAttribute("errorMessage", ex.getMessage());
        return "user/error";
    }
}
