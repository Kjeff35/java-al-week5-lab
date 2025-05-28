package org.bexos.social_media_app.service;

import org.bexos.social_media_app.dto.UserRegister;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface AuthService {
    String registerUser(UserRegister register, Model model, RedirectAttributes attributes) throws Exception;
}
