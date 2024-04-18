package com.example.gestion_user.repositories;

import com.example.gestion_user.entities.Case;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaseRepository extends JpaRepository <Case, Integer> {

    List<Case> getCasesByTitle(String titleCase);
    Case getCaseByTitle(String title);

}
