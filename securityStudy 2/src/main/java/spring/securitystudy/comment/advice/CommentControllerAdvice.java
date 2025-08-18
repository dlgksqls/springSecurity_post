package spring.securitystudy.comment.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.securitystudy.comment.exception.CommentNotFoundException;
import spring.securitystudy.exception.UserNotFoundException;

import java.nio.file.AccessDeniedException;

@ControllerAdvice(basePackages = "spring.securitystudy.comment.controller")
public class CommentControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public String UserNotFoundException(RuntimeException e, RedirectAttributes redirectAttributes){
        return "redirect:/user/login" + "?errorMessage=" + e.getMessage();
    }


    @ExceptionHandler({CommentNotFoundException.class, AccessDeniedException.class})
    public String CommentNotFoundAndAccessDeniedException(RuntimeException e,
                                        RedirectAttributes redirectAttributes,
                                        HttpServletRequest request){
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        String requestURI = request.getRequestURI();

        String[] urlSplit = requestURI.split("/");
        Long postId = Long.parseLong(urlSplit[3]);

        return "redirect:/post/detail/" + postId;
    }
}
