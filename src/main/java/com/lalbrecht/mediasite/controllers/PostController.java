package com.lalbrecht.mediasite.controllers;

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
    @PostMapping(value = "/new", consumes = "application.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Post newPost(@RequestHeader(name = "user_auth") String token, @RequestBody NewPostRequest req) {
        try {
            String uid = tokenServ.extractRequesterDetails(token).getId();
            return postServ.newPost(req, uid);
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

    @ExceptionHandler(value = InvalidRequestException.class)
    public ResponseEntity<Object> exception(InvalidRequestException exception) {
        return new ResponseEntity<>("Response not recognized",HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<Object> exception(AuthenticationException exception) {
        return new ResponseEntity<>("Access denied", HttpStatus.FORBIDDEN);
    }
}

