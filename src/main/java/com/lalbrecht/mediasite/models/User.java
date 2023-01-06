package com.lalbrecht.mediasite.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String user_id;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "salt", nullable = false)
    private byte[] salt;
    @Column(name = "email", nullable = true)
    private String email;
    @Column(name = "prestige", nullable = false)
    private int prestige;
    @Column(name = "mod", nullable = false)
    private boolean mod;
    @Column(name = "joined_date", nullable = false)
    private Date joined_date;

    @OneToMany (
            mappedBy = "user",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    @JsonManagedReference
    private List<Post> post;

    @OneToMany (
            mappedBy = "user",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    @JsonManagedReference
    private List<Comment> comment;

    public User(String user_id, String username, String password, byte[] salt, String email, int prestige, boolean mod, Date joined_date) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.email = email;
        this.prestige = prestige;
        this.mod = mod;
        this.joined_date = joined_date;
    }
}
