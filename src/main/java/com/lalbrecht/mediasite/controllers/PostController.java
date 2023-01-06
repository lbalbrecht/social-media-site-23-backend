package com.lalbrecht.mediasite.controllers;

import com.lalbrecht.mediasite.dtos.requests.FindPostRequest;
import com.lalbrecht.mediasite.dtos.requests.NewPostRequest;
import com.lalbrecht.mediasite.models.Post;
import com.lalbrecht.mediasite.services.PostService;
import com.lalbrecht.mediasite.services.TokenService;
import com.lalbrecht.mediasite.utils.custom_exceptions.AuthenticationException;
import com.lalbrecht.mediasite.utils.custom_exceptions.InvalidRequestException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;

@RestController
@RequestMapping("/post")
@AllArgsConstructor
public class PostController {
    @Autowired
    private final PostService postServ;
    private final TokenService tokenServ;

    @CrossOrigin
    @PostMapping(value = "/new", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Post newPost(@RequestBody NewPostRequest  post, HttpServletRequest req) {
        try {
            String uid = tokenServ.extractRequesterDetails(req.getHeader("user_auth")).getId();
            return postServ.newPost(post, uid);
        } catch (InvalidRequestException e) {
            e.getStackTrace();
            System.out.println(e.getMessage());
            throw new InvalidRequestException();
        } catch (AuthenticationException e) {
            e.getStackTrace();
            System.out.println(e.getMessage());
            throw new AuthenticationException();
        }
    }

    @CrossOrigin
    @PutMapping(value = "/like", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody void likePost(@RequestBody FindPostRequest post, HttpServletRequest req) {
        if (req.getHeader("user_auth") == null) {
            throw new AuthenticationException();
        } else {
            try {
                postServ.starPost(post.getPost_id());
                System.out.println(postServ.getPostById(post.getPost_id()).getStars());
            } catch (InvalidRequestException e) {
                e.getStackTrace();
                System.out.println(e.getMessage());
                throw new InvalidRequestException();
            }
        }
    }

    @ExceptionHandler(value = InvalidRequestException.class)
    public ResponseEntity<Object> exception(InvalidRequestException exception) {
        return new ResponseEntity<>("Response not recognized",HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<Object> exception(AuthenticationException exception) {
        return new ResponseEntity<>("Access denied", HttpStatus.FORBIDDEN);
    }
}

