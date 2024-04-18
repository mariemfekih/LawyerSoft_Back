package com.example.gestion_user.services.servicesImpl;

import com.example.gestion_user.entities.Court;
import com.example.gestion_user.repositories.CourtRepository;
import com.example.gestion_user.services.CourtService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CourtServicesImpl implements CourtService {


    @Autowired
    CourtRepository courtRepository;


    @Override
    public Court addCourt(Court court) {
        return courtRepository.save(court);
    }

    @Override
    public List<Court> getCourts() {
        return courtRepository.findAll();
    }
    @Override
    public Court getCourtById(Integer idCourt){
        return courtRepository.findById(idCourt).get();
    }
    @Override
    public Court updateCourt(Court court) {
        return courtRepository.save(court);
    }

    @Override
    public void deleteCourt(Integer idCourt) {
        courtRepository.deleteById(idCourt);
    }
}
