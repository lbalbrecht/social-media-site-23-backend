package com.lalbrecht.mediasite.controllers;

import com.lalbrecht.mediasite.dtos.requests.LoginRequest;
import com.lalbrecht.mediasite.dtos.requests.NewUserRequest;
import com.lalbrecht.mediasite.dtos.responses.Principal;
import com.lalbrecht.mediasite.services.TokenService;
import com.lalbrecht.mediasite.services.UserService;
import com.lalbrecht.mediasite.utils.custom_exceptions.InvalidRequestException;
import com.lalbrecht.mediasite.utils.custom_exceptions.ResourceConflictException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    @Autowired
    private final UserService userServ;
    private final TokenService tokenServ;

    @CrossOrigin
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/signin", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Principal login(@RequestBody LoginRequest request, HttpServletResponse resp) {
        try {
            Principal principal = userServ.login(request);
            String token = tokenServ.generateToken(principal);
            resp.setHeader("Authorization", token);
            resp.setHeader("Access-Control-Expose-Headers", "Authorization");
            return principal;
        } catch (InvalidRequestException e) {
            e.getStackTrace();
            System.out.println(e.getMessage());
            throw new InvalidRequestException();
        }
    }

    @CrossOrigin
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value = "/signup", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Principal signup(@RequestBody NewUserRequest request, HttpServletResponse resp) {
        try {
            Principal principal = userServ.register(request);
            String token = tokenServ.generateToken(principal);
            resp.setHeader("Authorization", token);
            resp.setHeader("Access-Control-Expose-Headers", "Authorization");
            return principal;
        } catch (InvalidRequestException e) {
            System.out.println(e.getMessage());
            throw new InvalidRequestException();
        } catch (ResourceConflictException e) {
            System.out.println(e.getMessage());
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
