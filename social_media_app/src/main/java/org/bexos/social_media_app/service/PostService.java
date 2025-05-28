package org.bexos.social_media_app.service;

import org.bexos.social_media_app.model.Post;

import java.util.List;

public interface PostService {
    Post createPost(Post post);

    List<Post> getAllPosts();

    Post getPostById(Long id);

    Post updatePost(Long id, Post post);

    void deletePost(Long id);
}
