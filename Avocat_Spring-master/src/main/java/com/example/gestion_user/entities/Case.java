package com.example.gestion_user.entities;


import com.example.gestion_user.entities.enums.CaseType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.security.SecureRandom;
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

    @NotBlank(message = "reference is requires")
    @Column(nullable = false,unique = true)
    private String reference;

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
    @OneToOne(mappedBy = "caseInstance", cascade = CascadeType.ALL)
    @JsonIgnore
    private Folder folder;

    /*
Relation entre User et Case
*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

/*
case-contributor
 */
    @OneToMany(mappedBy = "cases", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Contributor> contributors;

    // Constructor with random reference generation
    public Case(String title, String description, Date creationDate, Date closingDate, CaseType type) {
        this.title = title;
        this.description = description;
        this.creationDate = creationDate;
        this.closingDate = closingDate;
        this.type = type;
        this.reference = generateRandomReference();
    }

    // Method to generate random alphanumeric reference
    public String generateRandomReference() {
        int length = 6; // Adjust the length as needed
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }





}
