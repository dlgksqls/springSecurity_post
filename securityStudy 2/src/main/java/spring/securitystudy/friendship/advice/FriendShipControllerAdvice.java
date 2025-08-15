package spring.securitystudy.friendship.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.securitystudy.friendship.exception.HandleUserNotExistException;
import spring.securitystudy.friendship.exception.ReceiveUserNotFoundException;
import spring.securitystudy.user.exception.UserNotFoundException;

@ControllerAdvice(basePackages = "spring.securitystudy.friendship.controller")
public class FriendShipControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public String UserNotFoundException(RuntimeException ex, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("errorMessage", ex.getMessage());
        return "redirect:/user/login";
    }

    @ExceptionHandler(ReceiveUserNotFoundException.class)
    public String ReceiveUserNotFoundException(RuntimeException ex, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("errorMessage", ex.getMessage());
        return "redirect:/user/find";
    }

    @ExceptionHandler(HandleUserNotExistException.class)
    public String HandleUserNotExistException(RuntimeException ex, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("errorMessage", ex.getMessage());
        return "redirect:/friendship/receive";
    }
}
