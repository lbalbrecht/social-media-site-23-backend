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
@Table(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    private String comment_id;
    @Column(name = "commenter_id", nullable = false)
    private String commenter_id;
    @Column(name = "og_post_id", nullable = false)
    private String og_post_id;
    @Column(name = "reply_post_id", nullable = false)
    private String reply_post_id;
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

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    @JsonBackReference
    private Post post;

//    @OneToMany (
//            mappedBy = "comment",
//            fetch = FetchType.EAGER,
//            cascade = CascadeType.ALL
//    )
//
//    @ManyToOne
//    @JoinColumn(name = "comment_id", nullable = false)
//    @JsonBackReference
//    private Comment comment;
}
