package spring.securitystudy.user.advice;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.securitystudy.user.exception.TokenExpiredException;
import spring.securitystudy.user.exception.UserNotFoundException;
import spring.securitystudy.user.exception.UserAlreadyExistsException;

@ControllerAdvice(basePackages = "spring.securitystudy.user.controller")
public class UserControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public String UserNotFoundException(UserNotFoundException ex, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/user/error";
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public String UserAlreadyExistsException(UserAlreadyExistsException ex, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/user/error";
    }

    @ExceptionHandler(TokenExpiredException.class)
    public String TokenExpiredException(TokenExpiredException ex, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/user/error";
    }
}
