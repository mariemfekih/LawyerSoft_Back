package com.example.gestion_user.repositories;
import com.example.gestion_user.entities.Auxiliary;
import com.example.gestion_user.entities.Contributor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContributorRepository extends JpaRepository<Contributor, Integer> {
    List<Contributor> findByAuxiliary(Auxiliary auxiliary);
}
