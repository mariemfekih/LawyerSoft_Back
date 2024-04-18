package com.example.gestion_user.services;

import com.example.gestion_user.entities.Fee;

import java.util.List;

public interface FeeService {

    Fee addFee(Fee fee) ;

    List<Fee> getFees() ;

    Fee updateFee (Fee fee) ;

    void deleteFee (Integer idFee) ;

    Fee getFeeById (Integer idFee);

   // public void addHonoraireAndAffectToAffaire(Fee honoraire, Integer idAffaire);

    //public byte[] generateQRCodeForHonoraire(Integer idHonoraire) throws Exception;
}
