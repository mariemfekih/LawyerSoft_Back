package com.example.gestion_user.repositories;

import com.example.gestion_user.entities.Case;
import com.example.gestion_user.entities.Customer;
import com.example.gestion_user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByCin(String cin);
    Customer findByEmail(String email);
    Customer findByCases_IdCase(Long caseId);
    List<Customer> findByFolderIsNull();
    List<Customer> findCustomersByUserIdAndFolderIsNull( Long idUser);


}
