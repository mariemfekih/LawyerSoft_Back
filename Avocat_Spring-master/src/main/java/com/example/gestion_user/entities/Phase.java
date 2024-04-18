package com.example.gestion_user.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Phase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Integer idPhase;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date begginingDate;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date endingDate;


    //phase with trial
    @OneToMany(targetEntity = Trial.class,cascade = CascadeType.ALL)
    @JoinColumn(name="pt_fk", referencedColumnName = "idPhase")
    private List<Trial> trials;

    //phase with folder
    @OneToMany(targetEntity = Folder.class,cascade = CascadeType.ALL)
    @JoinColumn(name="pf_fk", referencedColumnName = "idPhase")
    private List<Folder> folders;


  /*  @OneToMany(mappedBy = "phase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dossier> dossiers;


    @ManyToMany
    @JoinTable(name = "phase_tribunal",
            joinColumns = @JoinColumn(name = "idPhase"),
            inverseJoinColumns = @JoinColumn(name = "idTribunal"))
    private List<Court> courts = new ArrayList<>();
*/

}
