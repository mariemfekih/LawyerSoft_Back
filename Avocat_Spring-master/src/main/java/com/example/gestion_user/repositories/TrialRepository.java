package com.example.gestion_user.repositories;

import com.example.gestion_user.entities.Trial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrialRepository extends JpaRepository<Trial, Long> {

}
