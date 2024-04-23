package com.example.gestion_user.entities;

import lombok.*;

import java.util.*;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "privileges")
public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    /*
    Relation entre User et privilege
    */
    @ManyToMany
    private List<User> users ;
}
