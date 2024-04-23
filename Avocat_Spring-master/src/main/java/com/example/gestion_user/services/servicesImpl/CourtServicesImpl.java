package com.example.gestion_user.services.servicesImpl;

import com.example.gestion_user.entities.Court;
import com.example.gestion_user.exceptions.NotFoundException;
import com.example.gestion_user.models.request.CourtDto;
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
    public Court addCourt(CourtDto c) {
        Court court=new Court();
        court.setAdress(c.getAdress());
        court.setPhone(c.getPhone());
        court.setGovernorate(c.getGovernorate());
        court.setMunicipality(c.getMunicipality());
        court.setType(c.getType());
        try {
            return courtRepository.save(court);
        } catch (Exception ex) {
            throw new NotFoundException("Failed to create court: " + ex.getMessage());
        }

    }

    @Override
    public Court updateCourt(Long id, CourtDto updatedCourtDto) {
        // Find the existing Court entity by ID
        Court existingCourt = courtRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Court not found with id: " + id));

        // Update the fields of the existing Court entity with values from the DTO
        existingCourt.setAdress(updatedCourtDto.getAdress());
        existingCourt.setPhone(updatedCourtDto.getPhone());
        existingCourt.setGovernorate(updatedCourtDto.getGovernorate());
        existingCourt.setMunicipality(updatedCourtDto.getMunicipality());
        existingCourt.setType(updatedCourtDto.getType());

        // Save the updated Court entity
        try {
            return courtRepository.save(existingCourt);
        } catch (Exception ex) {
            throw new NotFoundException("Failed to update court with id: " + id + ". " + ex.getMessage());
        }
    }


    @Override
    public void deleteCourt(Long idCourt) {
        courtRepository.deleteById(idCourt);
    }

    @Override
    public List<Court> getCourts() {
        return courtRepository.findAll();
    }

    @Override
    public Court getCourtById(Long idCourt) {
        return courtRepository.findById(idCourt).get();
    }
}

