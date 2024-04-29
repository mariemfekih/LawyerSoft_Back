package com.example.gestion_user.entities;


import com.example.gestion_user.entities.enums.CaseType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.*;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "cases")
public class Case implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( nullable = false, updatable = false)
    private Long idCase;

    @NotBlank(message = "title is requires")
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date creationDate;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date closingDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CaseType type;

    /*
    RELATION ENTRE CASE AND TRIAL
    */
    @OneToMany(mappedBy = "caseInstance")
    @JsonIgnore
    private List<Trial> trials;


    /*
   RELATION ENTRE CASE AND FOLDERS
   */
    @OneToMany(mappedBy = "caseInstance", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Folder> folders;

    /*
Relation entre User et Case
*/
    @ManyToMany
    private List<User> users ;

/*
case-contributor
 */
    @OneToMany(mappedBy = "cases", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Contributor> contributors;







}
