package com.lalbrecht.mediasite.controllers;

import com.lalbrecht.mediasite.dtos.requests.NewUserRequest;
import com.lalbrecht.mediasite.services.TokenService;
import com.lalbrecht.mediasite.services.UserService;
import com.lalbrecht.mediasite.utils.custom_exceptions.InvalidRequestException;
import com.lalbrecht.mediasite.utils.custom_exceptions.ResourceConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private final UserService userServ;
    private final TokenService tokenServ;

    public UserController(UserService userServ, TokenService tokenServ) {
        this.userServ = userServ;
        this.tokenServ = tokenServ;
    }

    @CrossOrigin
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value = "/signup", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String signup(@RequestBody NewUserRequest request) {
        try {
            return userServ.register(request).getUser_id();
        } catch (InvalidRequestException e) {
            e.getStackTrace();
            throw new InvalidRequestException();
        } catch (ResourceConflictException e) {
            e.getStackTrace();
            throw new ResourceConflictException();
        }
    }

    @ExceptionHandler(value = InvalidRequestException.class)
    public ResponseEntity<Object> exception(InvalidRequestException exception) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ResourceConflictException.class)
    public ResponseEntity<Object> exception(ResourceConflictException exception) {
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = HttpClientErrorException.Forbidden.class)
    public ResponseEntity<Object> exception(HttpClientErrorException exception) {
        return new ResponseEntity<>("Access Denied", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = HttpServerErrorException.BadGateway.class)
    public ResponseEntity<Object> exception(HttpServerErrorException exception) {
        return new ResponseEntity<>("An error occurred, please try again later", HttpStatus.BAD_GATEWAY);
    }
}
