package com.example.gestion_user.entities;

import com.example.gestion_user.entities.enums.FeeType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "fees")
public class Fee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long idFee;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String reference;

    @Column(nullable = false)
    private float amount;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String remain;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userInstance;

    @OneToMany(mappedBy = "feeInstance", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Action> actions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Fee(float amount, LocalDate date, String remain,String reference) {
        this.reference = reference;
        this.amount = amount;
        this.date = date;
        this.remain = remain;
    }


}
