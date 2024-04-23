package com.example.gestion_user.repositories;

import com.example.gestion_user.entities.Fee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeeRepository extends JpaRepository <Fee, Long> {
}
