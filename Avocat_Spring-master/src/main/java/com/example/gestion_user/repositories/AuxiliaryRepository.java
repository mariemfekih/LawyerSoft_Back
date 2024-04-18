package com.example.gestion_user.repositories;

import com.example.gestion_user.entities.Auxiliary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuxiliaryRepository extends JpaRepository<Auxiliary, Integer> {
    Auxiliary getAuxiliaryByCin(Long cin);
    Auxiliary getAuxiliaryByEmail(String email);

}
