package spring.securitystudy.advice;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import spring.securitystudy.exception.user.TokenExpiredException;
import spring.securitystudy.exception.user.UserNotFoundException;
import spring.securitystudy.user.exception.UserAlreadyExistsException;

@ControllerAdvice(basePackages = "spring.user.controller.UserController")
public class UserControllerAdvice {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public String UserAlreadyExistsException(RuntimeException ex, Model model){
        model.addAttribute("errorMessage", ex.getMessage());
        return "user/error";
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String UserNotFoundException(RuntimeException ex, Model model){
        model.addAttribute("errorMessage", ex.getMessage());
        return "user/error";
    }

    @ExceptionHandler(TokenExpiredException.class)
    public String TokenExpiredException(RuntimeException ex, Model model){
        model.addAttribute("errorMessage", ex.getMessage());
        return "user/error";
    }
}
