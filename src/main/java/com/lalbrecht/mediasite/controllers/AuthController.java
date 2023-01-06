package com.lalbrecht.mediasite.controllers;

import com.lalbrecht.mediasite.dtos.requests.LoginRequest;
import com.lalbrecht.mediasite.dtos.responses.Principal;
import com.lalbrecht.mediasite.services.TokenService;
import com.lalbrecht.mediasite.services.UserService;
import com.lalbrecht.mediasite.utils.custom_exceptions.InvalidRequestException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    @Autowired
    private final UserService userServ;
    private final TokenService tokenServ;

    @CrossOrigin(exposedHeaders = "user-auth")
    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Principal login(@RequestBody LoginRequest request, HttpServletResponse resp) {
        try {
            Principal principal = userServ.login(request);
            String token = tokenServ.generateToken(principal);
            resp.setHeader("user-auth", token);
            return principal;
        } catch (InvalidRequestException e) {
            e.getStackTrace();
            System.out.println(e.getMessage());
            throw new InvalidRequestException();
        }
    }

    @ExceptionHandler(value = InvalidRequestException.class)
    public ResponseEntity<Object> exception(InvalidRequestException exception) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
