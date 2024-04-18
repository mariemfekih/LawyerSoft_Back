package com.example.gestion_user.services;

import com.example.gestion_user.entities.Court;

import java.util.List;

public interface CourtService {

    Court addCourt(Court court) ;
    List<Court> getCourts() ;
    Court getCourtById(Integer idCourt);
    Court updateCourt(Court court);
    void deleteCourt(Integer idCourt);

}
