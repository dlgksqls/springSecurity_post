package spring.securitystudy.user.advice;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.securitystudy.user.exception.FindUserNotExistException;
import spring.securitystudy.user.exception.TokenExpiredException;
import spring.securitystudy.user.exception.UserNotFoundException;
import spring.securitystudy.user.exception.UserAlreadyExistsException;

@ControllerAdvice(basePackages = {"spring.securitystudy.user.controller"})
public class UserControllerAdvice {

    @ExceptionHandler(FindUserNotExistException.class)
    public String FindUserNotExistException(RuntimeException ex, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/user/find";
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String UserNotFoundException(RuntimeException ex, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/user/error";
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public String UserAlreadyExistsException(RuntimeException ex, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/user/register";
    }

    @ExceptionHandler(TokenExpiredException.class)
    public String TokenExpiredException(RuntimeException ex, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/user/error";
    }
}
