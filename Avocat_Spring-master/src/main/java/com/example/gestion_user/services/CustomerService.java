package com.example.gestion_user.services;

import com.example.gestion_user.entities.Auxiliary;
import com.example.gestion_user.entities.Case;
import com.example.gestion_user.entities.Customer;
import com.example.gestion_user.models.request.CustomerDto;

import java.util.List;

public interface CustomerService {
   // Customer addCustomer(CustomerDto a, Long userId);
   Customer addCustomer(CustomerDto a ,  Long userId) ;
   Customer updateCustomer (Long id, CustomerDto i) ;
    void deleteCustomer (Long idCustomer) ;
    List<Customer> getCustomers() ;
    Customer getCustomerById (Long idCustomer);
     List<Case> getCustomerCases(Long customerId);
     long getTotalCasesByCustomer(Long customerId);
     Customer getCustomerByCaseId(Long caseId);
    List<Customer> getUserCustomers(Long userId);
     List<Customer> getCustomersWithoutFolders();
    List<Customer> getCustomersWithoutFolders(Long idUser);
}
