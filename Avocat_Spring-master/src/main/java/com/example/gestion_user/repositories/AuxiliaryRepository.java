package com.example.gestion_user.repositories;

import com.example.gestion_user.entities.Auxiliary;
import com.example.gestion_user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuxiliaryRepository extends JpaRepository<Auxiliary, Long> {
    Auxiliary findByCin(String cin);
    Auxiliary findByEmail(String email);

    List<Auxiliary> findAllByUser(User user);
}
