package org.bexos.social_media_app.controller;

import lombok.RequiredArgsConstructor;
import org.bexos.social_media_app.dto.UserResponse;
import org.bexos.social_media_app.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final UserRepository userRepository;

    @GetMapping("/users")
    public List<UserResponse> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::new)
                .toList();
    }
}
