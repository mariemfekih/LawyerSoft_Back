package com.example.gestion_user.services;

import com.example.gestion_user.entities.Phase;

import java.util.List;

public interface PhaseService {
    Phase addPhase (Phase phase) ;

    List<Phase> getPhases() ;

    Phase updatePhase (Phase phase) ;

    void deletePhase (Integer idPhase) ;

    Phase getPhaseById (Integer idPhase);

    public Phase getPhaseByTitle(String title);
}
