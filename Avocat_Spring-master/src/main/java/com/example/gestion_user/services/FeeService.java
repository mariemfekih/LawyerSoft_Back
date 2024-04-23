package com.example.gestion_user.services;

import com.example.gestion_user.entities.Fee;
import com.example.gestion_user.models.request.FeeDto;

import java.util.List;

public interface FeeService {

    Fee addFee(FeeDto fee) ;
    Fee updateFee (Long id , FeeDto fee) ;
    void deleteFee (Long idFee) ;
    List<Fee> getFees() ;
    Fee getFeeById (Long idFee);

   // public void addHonoraireAndAffectToAffaire(Fee honoraire, Integer idAffaire);

    //public byte[] generateQRCodeForHonoraire(Integer idHonoraire) throws Exception;
}
