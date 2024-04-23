package com.example.gestion_user.services;

import com.example.gestion_user.entities.Court;
import com.example.gestion_user.models.request.CourtDto;

import java.util.List;

public interface CourtService {

    Court addCourt(CourtDto court) ;
    Court updateCourt(Long id ,CourtDto court);
    void deleteCourt(Long idCourt);
    List<Court> getCourts() ;
    Court getCourtById(Long idCourt);


}
