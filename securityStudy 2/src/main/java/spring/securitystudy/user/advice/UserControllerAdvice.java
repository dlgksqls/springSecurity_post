package spring.securitystudy.user.advice;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import spring.securitystudy.user.exception.UserAlreadyExistsException;

@ControllerAdvice(basePackages = "spring.securitystudy.user.controller")
public class UserControllerAdvice {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public String handleUserAlreadyExists(UserAlreadyExistsException ex, Model model){
        model.addAttribute("errorMsg", ex.getMessage());
        return "user/error";
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public String handlerUsernameNotFound(UsernameNotFoundException ex, Model model){
        model.addAttribute("errorMsg", ex.getMessage());
        return "user/error";
    }
}
