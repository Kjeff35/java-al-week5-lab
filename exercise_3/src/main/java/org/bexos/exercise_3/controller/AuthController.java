package org.bexos.exercise_3.controller;

import lombok.RequiredArgsConstructor;
import org.bexos.exercise_3.dto.UserRegister;
import org.bexos.exercise_3.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String getRegistration(Model model) {
        UserRegister user = new UserRegister();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") UserRegister user, Model model) throws Exception {
        authService.registerUser(user, model);
        return "redirect:/login";
    }
}
