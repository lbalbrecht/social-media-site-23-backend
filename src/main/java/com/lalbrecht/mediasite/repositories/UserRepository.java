package com.lalbrecht.mediasite.repositories;

import com.lalbrecht.mediasite.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    @Query(value = "SELECT * FROM users WHERE username = ?1 AND password = ?2", nativeQuery = true)
    User login(String username, String password);

    @Query(value = "SELECT username FROM users WHERE username = ?1", nativeQuery = true)
    String findUsername(String username);
}
