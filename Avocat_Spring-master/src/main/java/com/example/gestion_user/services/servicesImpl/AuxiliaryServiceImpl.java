package com.example.gestion_user.services.servicesImpl;

import com.example.gestion_user.entities.Auxiliary;
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
    public Auxiliary addAuxiliary(Auxiliary a) {
        return auxiliaryRepository.save(a);
    }

    @Override
    public List<Auxiliary> getAuxiliaries() {
        return auxiliaryRepository.findAll();
    }

    @Override
    public Auxiliary updateAuxiliary(Auxiliary i) {
        return auxiliaryRepository.save(i);
    }

    @Override
    public void deleteAuxiliary(Integer idAuxiliary) {
        auxiliaryRepository.deleteById(idAuxiliary);
    }

    @Override
    public Auxiliary getAuxiliaryById(Integer idAuxiliary) {
        return auxiliaryRepository.findById(idAuxiliary).get();
    }
    @Override
    public Auxiliary getAuxiliaryByCin(Long cin) {
        return auxiliaryRepository.getAuxiliaryByCin(cin);
    }
    public Auxiliary getAuxiliaryByEmail(String email) {
        return auxiliaryRepository.getAuxiliaryByEmail(email);
    }

}
