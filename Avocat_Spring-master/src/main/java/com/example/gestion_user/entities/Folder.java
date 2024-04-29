package com.example.gestion_user.entities;

import com.example.gestion_user.entities.enums.FolderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "folders")
public class Folder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long idFolder;

    @Column(nullable = false)
    @NotBlank
    private String name;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date creationDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FolderStatus status;

    /*
    RELATION ENTRE CASE AND FOLDER
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id") // foreign key column name
    private Case caseInstance;


    /*
        Relation entre Folder et File
        */
    @ManyToMany(mappedBy="folders")
    private List<File> files = new ArrayList<>();
}
