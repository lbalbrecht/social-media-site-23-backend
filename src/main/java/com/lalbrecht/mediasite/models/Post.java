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
public class Post {
    @Id
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

    public Post(String post_id, String poster_id, Date date_posted, String content, int stars, int replies) {
        this.post_id = post_id;
        this.poster_id = poster_id;
        this.date_posted = date_posted;
        this.content = content;
        this.stars = stars;
        this.replies = replies;
    }
}
