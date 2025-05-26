package org.bexos.exercise_3.service;

import org.bexos.exercise_3.dto.UserRegister;
import org.springframework.ui.Model;

public interface AuthService {
    void registerUser(UserRegister register, Model model) throws Exception;
}
