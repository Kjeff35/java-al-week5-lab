package org.bexos.social_media_app.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.bexos.social_media_app.dto.UserRegister;
import org.bexos.social_media_app.service.AuthService;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        HttpSession session, Model model) {
        if (error != null) {
            AuthenticationException ex = (AuthenticationException)
                    session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");

            String errorMessage = switch (ex.getClass().getSimpleName()) {
                case "BadCredentialsException" -> "Invalid username or password";
                case "DisabledException" -> "Your account is disabled";
                case "LockedException" -> "Your account is locked";
                case "AccountExpiredException" -> "Your account has expired";
                default -> "Login failed. Please try again.";
            };

            model.addAttribute("error", errorMessage);
        }
        return "login";
    }

    @GetMapping("/register")
    public String getRegistration(Model model) {
        UserRegister user = new UserRegister();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") UserRegister user, Model model, RedirectAttributes attributes) throws Exception {
        return authService.registerUser(user, model, attributes);
    }
}
