package com.lalbrecht.mediasite.repositories;

import com.lalbrecht.mediasite.models.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    @Query(value = "SELECT * FROM users WHERE username = ?1 AND password = ?2", nativeQuery = true)
    User login(String username, String password);

    @Query(value = "SELECT username FROM users WHERE username = ?1", nativeQuery = true)
    String findUsername(String username);

    @Query(value = "SELECT email FROM users WHERE email = ?1", nativeQuery = true)
    String findEmail(String email);

    @Query(value = "SELECT password FROM users WHERE user_id = ?1", nativeQuery = true)
    String getPassword(String id);

    @Query(value = "SELECT salt FROM users WHERE user_id = ?1", nativeQuery = true)
    byte[] getSaltById(String id);

    @Query(value = "SELECT salt FROM users WHERE username = ?1", nativeQuery = true)
    byte[] getSaltByUsername(String username);

    @Transactional
    @Modifying
    @Query(value = "UPDATE users SET username = ?2 WHERE user_id = ?1", nativeQuery = true)
    void changeUsername(String id, String username);

    @Transactional
    @Modifying
    @Query(value = "UPDATE users SET (password, salt) = (?2, ?3) WHERE user_id = ?1", nativeQuery = true)
    void changePassword(String id, String password, byte[] salt);

    @Transactional
    @Modifying
    @Query(value = "UPDATE users SET email = ?2 WHERE user_id = ?1", nativeQuery = true)
    void changeEmail(String id, String email);
}
