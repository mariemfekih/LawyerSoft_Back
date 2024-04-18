package com.example.gestion_user.repositories;

import com.example.gestion_user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User getUserByUsername(String username);
    User getUserByEmail(String email);

}
