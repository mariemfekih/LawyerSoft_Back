package com.example.gestion_user.services;

import com.example.gestion_user.entities.Auxiliary;

import java.util.List;

public interface AuxiliaryService {
    Auxiliary addAuxiliary (Auxiliary a) ;

    List<Auxiliary> getAuxiliaries() ;

    Auxiliary updateAuxiliary (Auxiliary i) ;

    void deleteAuxiliary (Integer idAuxiliary) ;

    Auxiliary getAuxiliaryById (Integer idAuxiliary);
    Auxiliary getAuxiliaryByCin(Long cin);

    Auxiliary getAuxiliaryByEmail(String email);
}
