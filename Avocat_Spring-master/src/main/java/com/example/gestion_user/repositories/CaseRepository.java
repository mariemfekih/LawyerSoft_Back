package com.example.gestion_user.repositories;

import com.example.gestion_user.entities.Case;
import com.example.gestion_user.entities.Customer;
import com.example.gestion_user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface CaseRepository extends JpaRepository <Case, Long> {
    List<Case> findByFolderIsNull();
    List<Case> findAllByUser(User user);
    @Query("SELECT c FROM Case c WHERE c.user = :user AND c.creationDate >= :startDate AND c.creationDate <= :endDate")
    List<Case> findAllByUserAndCreationDateBetween(@Param("user") User user, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
    @Query("SELECT c FROM Case c WHERE c.user = :user AND c.creationDate >= :startDate AND c.creationDate <= :endDate")
    List<Case> findAllByUserAndCreationDateBetween(@Param("user") User user, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    List<Case> findAllByTitle(String titleCase);
    Case findByTitle(String title);

    List<Case> findAllByCustomer(Customer customer);
    List<Case> findByUserId(Long userId);

        @Query("SELECT c FROM Case c JOIN FETCH c.customer WHERE c.idCase = :idCase")
        Optional<Case> findByIdCaseWithCustomer(@Param("idCase") Long idCase);

    }
