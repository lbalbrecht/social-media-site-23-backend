package com.lalbrecht.mediasite.controllers;

import com.lalbrecht.mediasite.dtos.requests.NewPasswordRequest;
import com.lalbrecht.mediasite.dtos.requests.NewUserRequest;
import com.lalbrecht.mediasite.dtos.requests.UserInfoRequest;
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

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    @Autowired
    private final UserService userServ;
    private final TokenService tokenServ;

    @CrossOrigin
    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping(value = "update_info", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String updateUserInfo(@RequestBody UserInfoRequest request, @RequestParam(name = "info") String info, @RequestHeader(name = "Authorization") String token) {
        Principal principal = tokenServ.extractRequesterDetails(token);
        String userId = principal.getId();
        String updatedInfo = request.getRequest();

        try {
            switch(info) {
                case "username":
                    userServ.updateUsername(userId, updatedInfo);
                    return "Username successfully updated";
                case "email":
                    userServ.updateEmail(userId, updatedInfo);
                    return "Email successfully updated";
                default:
                    throw new HttpServerErrorException(HttpStatus.BAD_GATEWAY);
            }
        } catch (InvalidRequestException e) {
            System.out.println(e.getMessage());
            throw new InvalidRequestException();
        } catch (ResourceConflictException e) {
            System.out.println(e.getMessage());
            throw new ResourceConflictException();
        }
    }

    @CrossOrigin
    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping(value = "update_password", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String updateUserPassword(@RequestBody NewPasswordRequest request, @RequestHeader(name = "user-auth") String token) {
        Principal principal = tokenServ.extractRequesterDetails(token);
        String userId = principal.getId();

        try {
            userServ.matchesExistingPassword(userId,request.getOldPassword());
            userServ.updatePassword(userId, request.getPassword1(), request.getPassword2());
            return "Password successfully updated";
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
