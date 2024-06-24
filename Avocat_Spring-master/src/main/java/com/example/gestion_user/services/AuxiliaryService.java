package com.example.gestion_user.services;

import com.example.gestion_user.entities.Auxiliary;
import com.example.gestion_user.models.request.AuxiliaryDto;

import java.util.List;

public interface AuxiliaryService {
    Auxiliary addAuxiliary(AuxiliaryDto a, Long userId);
    Auxiliary updateAuxiliary (Long id,AuxiliaryDto i) ;
    void deleteAuxiliary (Long idAuxiliary) ;
    List<Auxiliary> getAuxiliaries() ;
    Auxiliary getAuxiliaryById (Long idAuxiliary);
    List<Auxiliary> getUserAuxiliaries(Long userId);
    Auxiliary getAuxiliaryByCin(String cin);
    Auxiliary getAuxiliaryByEmail(String email);

    long getTotalAuxiliariesByUser(Long userId);
}
