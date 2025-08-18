package spring.securitystudy.friendship.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.securitystudy.exception.UserNotFoundException;
import spring.securitystudy.friendship.exception.FriendShipNotFoundException;
import spring.securitystudy.friendship.exception.HandleUserNotExistException;
import spring.securitystudy.friendship.exception.ReceiveUserNotFoundException;

@ControllerAdvice(basePackages = "spring.securitystudy.friendship.controller")
public class FriendShipControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public String UserNotFoundException(RuntimeException e, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/user/login";
    }

    @ExceptionHandler(ReceiveUserNotFoundException.class)
    public String ReceiveUserNotFoundException(RuntimeException e, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/user/find";
    }

    @ExceptionHandler(HandleUserNotExistException.class)
    public String HandleUserNotExistException(RuntimeException e, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/friendship/receive";
    }

    @ExceptionHandler(FriendShipNotFoundException.class)
    public String FriendShipNotFoundException(RuntimeException e, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/friendship/receive";
    }
}
