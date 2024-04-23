package com.example.gestion_user.repositories;

import com.example.gestion_user.entities.Auxiliary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuxiliaryRepository extends JpaRepository<Auxiliary, Long> {
    Auxiliary findByCin(String cin);
    Auxiliary findByEmail(String email);

}
