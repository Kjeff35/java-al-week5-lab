package org.bexos.social_media_app.repository;

import org.bexos.social_media_app.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
