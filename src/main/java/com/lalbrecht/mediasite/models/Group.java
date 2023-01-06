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
@Table(name = "groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Group {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private String group_id;
    @Column(name = "group_name", nullable = false)
    private String group_name;
    @Column(name = "group_description", nullable = false)
    private String group_description;
    @Column(name = "group_members", nullable = false)
    private int group_members;
    @Column(name = "date_created", nullable = false)
    private Date date_created;
    @Column(name = "is_private", nullable = false)
    private boolean is_private;
}
