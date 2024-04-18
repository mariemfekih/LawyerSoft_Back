package com.example.gestion_user.repositories;

import com.example.gestion_user.entities.Phase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhaseRepository extends JpaRepository<Phase, Integer> {
    Phase getPhaseByTitle(String titrePhase);

}
