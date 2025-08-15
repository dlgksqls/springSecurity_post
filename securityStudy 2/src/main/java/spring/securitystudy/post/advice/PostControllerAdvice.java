package spring.securitystudy.post.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.securitystudy.post.exception.PostDeniedException;
import spring.securitystudy.post.exception.PostNotFoundException;
import spring.securitystudy.user.exception.UserNotAuthenticatedException;

@ControllerAdvice(basePackages = "spring.securitystudy.post.controller")
public class PostControllerAdvice {

    @ExceptionHandler(PostDeniedException.class)
    public String PostDeniedException(PostDeniedException ex, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("errorMessage", ex.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler(PostNotFoundException.class)
    public String PostNotFoundException(PostNotFoundException ex, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("errorMessage", ex.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler(UserNotAuthenticatedException.class)
    public String UserNotAuthenticatedException(UserNotAuthenticatedException ex, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("errorMessage", ex.getMessage());
        return "redirect:/login.html";
    }
}
