package com.lalbrecht.mediasite.services;

import com.lalbrecht.mediasite.dtos.requests.LoginRequest;
import com.lalbrecht.mediasite.dtos.requests.NewUserRequest;
import com.lalbrecht.mediasite.dtos.responses.Principal;
import com.lalbrecht.mediasite.models.User;
import com.lalbrecht.mediasite.repositories.UserRepository;
import com.lalbrecht.mediasite.utils.HashConfig;
import com.lalbrecht.mediasite.utils.custom_exceptions.InvalidRequestException;
import com.lalbrecht.mediasite.utils.custom_exceptions.ResourceConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private final UserRepository userRepo;
    private final HashConfig hash;

    public UserService(UserRepository userRepo, HashConfig hash) {
        this.userRepo = userRepo;
        this.hash = hash;
    }

    public Principal register(NewUserRequest request) {
        User user = null;
        if (isValidUsername(request.getUsername())) {
            if (isDuplicateUsername(request.getUsername())) {
                if (isValidPassword(request.getPassword1())) {
                    if (isSamePassword(request.getPassword1(), request.getPassword2())) {
                        byte[] salt = hash.generateSalt();
                        String userPass = hash.hashPassword(request.getPassword1(), salt);
                        user = new User(
                                UUID.randomUUID().toString(),
                                request.getUsername(),
                                userPass,
                                salt,
                                "",
                                0,
                                false,
                                new java.sql.Date(new Date().getTime())
                        );
                        userRepo.save(user);
                    }
                }
            }
        }
        assert user != null;
        return new Principal(user.getUser_id(), user.getUsername(), user.isMod());
    }

    public Principal login(LoginRequest req) {
        byte[] salt = userRepo.getSaltByUsername(req.getUsername());
        String saltyPass = hash.hashPassword(req.getPassword(), salt);

        User user = userRepo.login(req.getUsername(), saltyPass);

        if (user != null) {
            return new Principal(user.getUser_id(), user.getUsername(), user.isMod());
        } else {
            throw new InvalidRequestException("\nUser not found with those credentials");
        }
    }

    public void updateUsername(String id, String username) {
        if (isValidUsername(username)) {
            if (isDuplicateUsername(username)) {
                userRepo.changeUsername(id, username);
            }
        }
    }

    public void updatePassword(String id, String password1, String password2) {
        if (isValidPassword(password1)) {
            if (isSamePassword(password1, password2)) {
                byte[] salt = hash.generateSalt();
                String newPass = hash.hashPassword(password1, salt);
                userRepo.changePassword(id, newPass, salt);
            }
        }
    }

    public void updateEmail(String id, String email) {
        if (isValidEmail(email)) {
            if (isDuplicateEmail(email)) {
                userRepo.changeEmail(id, email);
            }
        }
    }

    public boolean isValidUsername(String username) {
        if (!username.matches("^[a-zA-Z0-9_-]{3,15}$"))
            throw new InvalidRequestException("\nUsername must be between 3-15 characters and may only contain letters, numbers, dashes, and hyphens");
        return true;
    }


    public boolean isValidEmail(String email) {
        if (!email.matches("[A-Za-z0-9._-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}"))
            throw new InvalidRequestException("\nPlease enter a valid email address");
        return true;
    }

    public boolean isDuplicateEmail(String email) {
        if (userRepo.findEmail(email) != null) throw new ResourceConflictException("\nEmail not available!");
        return true;
    }

    public boolean isValidPassword(String password) {
        if (!password.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,20}$"))
            throw new InvalidRequestException("\nPassword must be 8-20 characters with at least one uppercase letter, one lowercase letter, one number, and one special character");
        return true;
    }

    public boolean isSamePassword(String password, String password2) {
        if (!password2.equals(password)) throw new InvalidRequestException("Passwords do not match!");
        return true;
    }

    public boolean isDuplicateUsername(String username) {
        if (userRepo.findUsername(username) != null) throw new ResourceConflictException("\nUsername not available!");
        return true;
    }

        public boolean matchesExistingPassword(String id, String password) {
        String userPass = userRepo.getPassword(id);
        byte[] userSalt = userRepo.getSaltById(id);

        String saltyPass = hash.hashPassword(password, userSalt);
        if(!saltyPass.equals(userPass)) {
            throw new InvalidRequestException ("Password not recognized");
        }
        return true;
    }
}
