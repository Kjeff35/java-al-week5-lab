package org.bexos.social_media_app;

import lombok.RequiredArgsConstructor;
import org.bexos.social_media_app.model.User;
import org.bexos.social_media_app.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class SocialMediaAppApplication implements CommandLineRunner {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SocialMediaAppApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		User user1 = User.builder()
				.firstName("user1")
				.lastName("user1")
				.email("user1@example.com")
				.password(passwordEncoder.encode("user1@example.com"))
				.build();

		User user2 = User.builder()
				.firstName("user2")
				.lastName("user2")
				.email("user2@example.com")
				.password(passwordEncoder.encode("user2@example.com"))
				.build();

		userRepository.saveAll(List.of(user1, user2));
	}
}
