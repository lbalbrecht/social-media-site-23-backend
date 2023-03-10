package com.lalbrecht.mediasite.services;

import com.lalbrecht.mediasite.dtos.requests.NewPostRequest;
import com.lalbrecht.mediasite.models.Post;
import com.lalbrecht.mediasite.models.User;
import com.lalbrecht.mediasite.repositories.PostRepository;
import com.lalbrecht.mediasite.repositories.UserRepository;
import com.lalbrecht.mediasite.utils.custom_exceptions.AuthenticationException;
import com.lalbrecht.mediasite.utils.custom_exceptions.InvalidRequestException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PostService {
    @Autowired
    private final PostRepository postRepo;
    private final UserRepository userRepo;

    public Post newPost(NewPostRequest req, String uid) {
        Post post = null;
        if (userRepo.findById(uid).isPresent()) {
            User user = userRepo.findById(uid).get();
            if (isValidLength(req.getContent())) {
                post = new Post(
                        UUID.randomUUID().toString(),
                        new java.sql.Date(new Date().getTime()),
                        req.getContent(),
                        0,
                        0,
                        user
                );
                postRepo.save(post);
            }
        } else {
            throw new AuthenticationException("\nYou must be logged in to post!");
        }
        return post;
    }

    public void starPost(String pid) {
        if (postRepo.findById(pid).isPresent()) {
            int stars = postRepo.getPostLikes(pid);
            postRepo.starPost(pid, (stars + 1));
        } else {
            throw new InvalidRequestException("\nPost not found");
        }
    }

    public Post getPostById(String pid) {
        if (postRepo.findById(pid).isPresent()) {
            return postRepo.findById(pid).get();
        } else {
            throw new InvalidRequestException("\nPost not found");
        }
    }

    public boolean isValidLength(String post) {
        if (post.length() == 0) {
            throw new InvalidRequestException("\nYour post has nothing in it!");
        }
        else if (post.length() < 5 || post.length() > 2000) {
            throw new InvalidRequestException("\n Your post must be between 5 and 2000 characters!");
        } else {
            return true;
        }
    }
}