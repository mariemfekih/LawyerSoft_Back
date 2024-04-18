package com.example.gestion_user.repositories;

import com.example.gestion_user.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RdvRepository extends JpaRepository<Appointment, Integer> {
}
