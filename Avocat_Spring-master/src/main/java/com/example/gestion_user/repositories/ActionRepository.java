package com.example.gestion_user.repositories;

import com.example.gestion_user.entities.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {

    @Query(value = "SELECT * FROM actions WHERE id_action IN (:ids)", nativeQuery = true)
    List<Action> temp(@Param("ids") List<Long> ids);
}
