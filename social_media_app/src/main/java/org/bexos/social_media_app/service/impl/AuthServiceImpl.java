package org.bexos.social_media_app.service.impl;

import lombok.RequiredArgsConstructor;
import org.bexos.social_media_app.dto.UserRegister;
import org.bexos.social_media_app.model.User;
import org.bexos.social_media_app.repository.UserRepository;
import org.bexos.social_media_app.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String registerUser(UserRegister register, Model model, RedirectAttributes redirectAttributes) throws Exception {
        if (!register.getPassword().equals(register.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match");
            return "/register";
        }
        if (userRepository.existsByEmailIgnoreCase(register.getEmail())) {
            model.addAttribute("error", "User with this email already exists.");
            return "/register";
        }
        User user = new User();
        user.setEmail(register.getEmail());
        user.setPassword(passwordEncoder.encode(register.getPassword()));
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("success", "User created successfully.");
        return "redirect:/login";
    }
}
