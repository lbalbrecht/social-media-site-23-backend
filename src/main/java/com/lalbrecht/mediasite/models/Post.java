package com.lalbrecht.mediasite.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Post {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private String post_id;
    @Column(name = "poster_id", nullable = false)
    private String poster_id;
    @Column(name = "date_posted", nullable = false)
    private Date date_posted;
    @Column(name = "content", nullable = false)
    private String content;
    @Column(name = "stars", nullable = false)
    private int stars;
    @Column(name = "replies", nullable = false)
    private int replies;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @OneToMany (
            mappedBy = "post",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    @JsonManagedReference
    private List<Comment> comment;
}
