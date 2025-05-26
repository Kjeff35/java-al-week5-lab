package org.bexos.exercise_3.service.impl;

import lombok.RequiredArgsConstructor;
import org.bexos.exercise_3.dto.UserRegister;
import org.bexos.exercise_3.model.User;
import org.bexos.exercise_3.repository.UserRepository;
import org.bexos.exercise_3.service.AuthService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(UserRegister register, Model model) throws Exception {
        if (!register.getPassword().equals(register.getConfirmPassword())){
            model.addAttribute("error", "Passwords do not match");
            return;
        }
        if (userRepository.existsByEmailIgnoreCase(register.getEmail())) {
            model.addAttribute("error", "User with this email already exists.");
            return;
        }
        String encryptedSecret = encryptWithBouncyCastle("SensitiveBankSecret" + register.getEmail());
        User user = new User();
        user.setEmail(register.getEmail());
        user.setPassword(passwordEncoder.encode(register.getPassword()));
        user.setEncryptedSecret(encryptedSecret);
        userRepository.save(user);
    }

    private String encryptWithBouncyCastle(String data) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "BC");
        SecretKeySpec key = new SecretKeySpec("1234567890123456".getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }
}
