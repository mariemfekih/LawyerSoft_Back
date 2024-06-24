package com.example.gestion_user.services.servicesImpl;

import com.example.gestion_user.entities.Auxiliary;
import com.example.gestion_user.entities.Case;
import com.example.gestion_user.entities.Contributor;
import com.example.gestion_user.entities.User;
import com.example.gestion_user.exceptions.NotFoundException;
import com.example.gestion_user.models.request.AuxiliaryDto;
import com.example.gestion_user.repositories.AuxiliaryRepository;
import com.example.gestion_user.repositories.ContributorRepository;
import com.example.gestion_user.repositories.UserRepository;
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
    @Autowired
    UserRepository userRepository;
    @Autowired
    ContributorRepository contributorRepository;
    @Override
    public Auxiliary addAuxiliary(AuxiliaryDto a, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        Auxiliary existingAuxiliary = auxiliaryRepository.findByCin(a.getCin());
        if (existingAuxiliary != null) {
            throw new DuplicateCinException("CIN " + a.getCin() + " already exists.");
        }

        Auxiliary auxiliary = new Auxiliary();
        auxiliary.setFirstName(a.getFirstName());
        auxiliary.setLastName(a.getLastName());
        auxiliary.setCin(a.getCin());
        auxiliary.setEmail(a.getEmail());
        auxiliary.setJob(a.getJob());
        auxiliary.setPhone(a.getPhone());
        auxiliary.setCity(a.getCity());
        auxiliary.setBirthDate(a.getBirthDate());
        auxiliary.setGender(a.getGender());
        auxiliary.setUser(user);

        try {
            return auxiliaryRepository.save(auxiliary);
        } catch (Exception ex) {
            throw new NotFoundException("Failed to create auxiliary: " + ex.getMessage());
        }
    }

    public class DuplicateCinException extends RuntimeException {
        public DuplicateCinException(String message) {
            super(message);
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
        existingAuxiliary.setGender(updatedAuxiliaryDto.getGender());

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
    @Override
    public List<Auxiliary> getUserAuxiliaries(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        return user.getAuxiliaries();
    }
    @Override
    public Auxiliary getAuxiliaryByCin(String cin) {
        return auxiliaryRepository.findByCin(cin);
    }
    public Auxiliary getAuxiliaryByEmail(String email) {
        return auxiliaryRepository.findByEmail(email);
    }

    @Override
    public long getTotalAuxiliariesByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found !!"));
        List<Auxiliary> auxiliaries = auxiliaryRepository.findAllByUser(user);
        return auxiliaries.size();
    }

}
