package spring.securitystudy.verificationToken.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.securitystudy.user.CustomUserDetails;
import spring.securitystudy.user.entity.User;
import spring.securitystudy.user.service.UserService;
import spring.securitystudy.verificationToken.service.VerificationService;

@Controller
@RequiredArgsConstructor
public class VerificationController {

    private final VerificationService verificationService;
    private final UserService userService;

    @GetMapping("/verified/check-email")
    public String checkEmailPage(@AuthenticationPrincipal CustomUserDetails userDetails,
                                 RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("isEnable", userDetails.getUser().isEnable());
        return "verified/check-email";
    }

    @GetMapping("/verify")
    public String verifyEmail(@AuthenticationPrincipal CustomUserDetails userDetails,
                              @RequestParam String token,
                              RedirectAttributes redirectAttributes){
        boolean verified = verificationService.verifyToken(token);

        if (verified){
            return "redirect:/user/login";
        }
        else{
            redirectAttributes.addFlashAttribute("isEnable", userDetails.getUser().isEnable());
            return "redirect:/verified/check-email?error=true";
        }
    }

    @GetMapping("/sendMail")
    public String sendMail(@AuthenticationPrincipal CustomUserDetails userDetails,
                           RedirectAttributes redirectAttributes){
        User loginUser = userService.findByUsername(userDetails.getUsername());
        if (!loginUser.isEnable()){
            verificationService.sendVerificationEmail(loginUser);
        }
        redirectAttributes.addFlashAttribute("isEnable", userDetails.getUser().isEnable());
        return "redirect:/verified/check-email";
    }
}
