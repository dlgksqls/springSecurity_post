package spring.securitystudy.verificationToken.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spring.securitystudy.user.CustomUserDetails;
import spring.securitystudy.user.entity.User;
import spring.securitystudy.user.repository.UserRepository;
import spring.securitystudy.user.service.UserService;
import spring.securitystudy.verificationToken.service.VerificationService;

@Controller
@RequiredArgsConstructor
public class VerificationController {

    private final VerificationService verificationService;
    private final UserService userService;

    @GetMapping("/user/check-email")
    public String checkEmailPage(){
        return "user/check-email";
    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam String token){
        boolean verified = verificationService.verifyToken(token);

        if (verified){
            return "redirect:/user/login";
        }
        else{
            return "redirect:/user/check-email?error=true";
        }
    }

    @GetMapping("/sendMail")
    public String sendMail(@AuthenticationPrincipal CustomUserDetails user){
        User loginUser = userService.findByUsername(user.getUsername());
        if (loginUser == null || loginUser.isEnable()) {
            return "redirect:/user/login?error=true&message=Invalid Access";
        }
        verificationService.sendVerificationEmail(loginUser);
        return "redirect:/user/check-email";
    }
}
