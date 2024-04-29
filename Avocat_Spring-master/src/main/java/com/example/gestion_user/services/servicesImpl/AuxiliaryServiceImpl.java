package com.example.gestion_user.services.servicesImpl;

import com.example.gestion_user.entities.Auxiliary;
import com.example.gestion_user.exceptions.NotFoundException;
import com.example.gestion_user.models.request.AuxiliaryDto;
import com.example.gestion_user.repositories.AuxiliaryRepository;
import com.example.gestion_user.services.AuxiliaryService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@AllArgsConstructor
@Service
public class AuxiliaryServiceImpl implements AuxiliaryService {
    @Autowired
    AuxiliaryRepository auxiliaryRepository;
    @Override
    public Auxiliary addAuxiliary(AuxiliaryDto a) {
        Auxiliary auxiliary=new Auxiliary();
        auxiliary.setFirstName(a.getFirstName());
        auxiliary.setLastName(a.getLastName());
        auxiliary.setCin(a.getCin());
        auxiliary.setEmail(a.getEmail());
        auxiliary.setJob(a.getJob());
        auxiliary.setPhone(a.getPhone());
        auxiliary.setCity(a.getCity());
        auxiliary.setBirthDate(a.getBirthDate());

        try {
            return auxiliaryRepository.save(auxiliary);
        } catch (Exception ex) {
            throw new NotFoundException("Failed to create auxiliary: " + ex.getMessage());
        }
    }
    @Override
    public Auxiliary updateAuxiliary(Long id, AuxiliaryDto updatedAuxiliaryDto) {
        // Find the existing Auxiliary entity by ID
        Auxiliary existingAuxiliary = auxiliaryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Auxiliary not found with id: " + id));

        // Update the fields of the existing Auxiliary entity with values from the DTO
        existingAuxiliary.setFirstName(updatedAuxiliaryDto.getFirstName());
        existingAuxiliary.setLastName(updatedAuxiliaryDto.getLastName());
        existingAuxiliary.setCin(updatedAuxiliaryDto.getCin());
        existingAuxiliary.setEmail(updatedAuxiliaryDto.getEmail());
        existingAuxiliary.setJob(updatedAuxiliaryDto.getJob());
        existingAuxiliary.setPhone(updatedAuxiliaryDto.getPhone());
        existingAuxiliary.setCity(updatedAuxiliaryDto.getCity());
        existingAuxiliary.setBirthDate(updatedAuxiliaryDto.getBirthDate());

        // Save the updated Auxiliary entity
        try {
            return auxiliaryRepository.save(existingAuxiliary);
        } catch (Exception ex) {
            throw new NotFoundException("Failed to update auxiliary with id: " + id + ". " + ex.getMessage());
        }
    }
    @Override
    public void deleteAuxiliary(Long idAuxiliary) {
        auxiliaryRepository.deleteById(idAuxiliary);
    }

    @Override
    public List<Auxiliary> getAuxiliaries() {
        return auxiliaryRepository.findAll();
    }
    @Override
    public Auxiliary getAuxiliaryById(Long idAuxiliary) {
        return auxiliaryRepository.findById(idAuxiliary).get();
    }
   /* @Override
    public Auxiliary getAuxiliaryByCin(String cin) {
        return auxiliaryRepository.findByCin(cin);
    }
    public Auxiliary getAuxiliaryByEmail(String email) {
        return auxiliaryRepository.findByEmail(email);
    }*/

}
