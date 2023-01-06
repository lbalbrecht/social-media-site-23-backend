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
@Table(name = "tags")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Tag {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private String tag_id;
    @Column(name = "tag_name", nullable = false)
    private String tag_name;
}
