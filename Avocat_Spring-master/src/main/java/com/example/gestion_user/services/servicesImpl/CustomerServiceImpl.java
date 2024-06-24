package com.example.gestion_user.services.servicesImpl;

import com.example.gestion_user.entities.Auxiliary;
import com.example.gestion_user.entities.Case;
import com.example.gestion_user.entities.Customer;
import com.example.gestion_user.entities.User;
import com.example.gestion_user.exceptions.NotFoundException;
import com.example.gestion_user.models.request.CustomerDto;
import com.example.gestion_user.repositories.CaseRepository;
import com.example.gestion_user.repositories.CustomerRepository;
import com.example.gestion_user.repositories.UserRepository;
import com.example.gestion_user.services.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CaseRepository caseRepository;
    @Autowired
    UserRepository userRepository;
    @Override
    public Customer addCustomer(CustomerDto a ,  Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        Customer existingCustomer = customerRepository.findByCin(a.getCin());
        if (existingCustomer != null) {
            throw new DuplicateCinException("CIN " + a.getCin() + " already exists.");
        }

        // Create a new customer and set its fields from the DTO
        Customer customer = new Customer();
        customer.setFirstName(a.getFirstName());
        customer.setLastName(a.getLastName());
        customer.setCin(a.getCin());
        customer.setEmail(a.getEmail());
        customer.setJob(a.getJob());
        customer.setPhone(a.getPhone());
        customer.setCity(a.getCity());
        customer.setBirthDate(a.getBirthDate());
        customer.setGender(a.getGender());
        customer.setUser(user);

        try {
            // Save the customer
            return customerRepository.save(customer);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to create customer: " + ex.getMessage());
        }
    }

//    @Override
//    public Customer addCustomer(CustomerDto a, Long caseId) {
//        // Retrieve the case instance by ID, throw an exception if not found
//        Case caseInstance = caseRepository.findById(caseId)
//                .orElseThrow(() -> new NotFoundException("Case not found"));
//
//        // Check if the case already has a customer assigned
//        if (caseInstance.getCustomer() != null) {
//            throw new RuntimeException("Case already has a customer assigned");
//        }
//
//        // Check if a customer with the same CIN already exists
//        Customer existingCustomer = customerRepository.findByCin(a.getCin());
//        if (existingCustomer != null) {
//            throw new DuplicateCinException("CIN " + a.getCin() + " already exists.");
//        }
//
//        // Create a new customer and set its fields from the DTO
//        Customer customer = new Customer();
//        customer.setFirstName(a.getFirstName());
//        customer.setLastName(a.getLastName());
//        customer.setCin(a.getCin());
//        customer.setEmail(a.getEmail());
//        customer.setJob(a.getJob());
//        customer.setPhone(a.getPhone());
//        customer.setCity(a.getCity());
//        customer.setBirthDate(a.getBirthDate());
//        customer.setGender(a.getGender());
//
//        // Set the customer to the case and add the case to the customer's list of cases
//        caseInstance.setCustomer(customer);
//        customer.getCases().add(caseInstance);
//
//        try {
//            // Save the customer and the case
//            customerRepository.save(customer);
//            caseRepository.save(caseInstance);
//            return customer;
//        } catch (Exception ex) {
//            throw new RuntimeException("Failed to create customer: " + ex.getMessage());
//        }
//    }
//

    public class DuplicateCinException extends RuntimeException {
        public DuplicateCinException(String message) {
            super(message);
        }
    }
    @Override
    public Customer updateCustomer(Long id, CustomerDto updatedCustomerDto) {
        // Find the existing Customer entity by ID
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found with id: " + id));

        // Update the fields of the existing Customer entity with values from the DTO
        existingCustomer.setFirstName(updatedCustomerDto.getFirstName());
        existingCustomer.setLastName(updatedCustomerDto.getLastName());
        existingCustomer.setCin(updatedCustomerDto.getCin());
        existingCustomer.setEmail(updatedCustomerDto.getEmail());
        existingCustomer.setJob(updatedCustomerDto.getJob());
        existingCustomer.setPhone(updatedCustomerDto.getPhone());
        existingCustomer.setCity(updatedCustomerDto.getCity());
        existingCustomer.setBirthDate(updatedCustomerDto.getBirthDate());
        existingCustomer.setGender(updatedCustomerDto.getGender());

        // Save the updated Customer entity
        try {
            return customerRepository.save(existingCustomer);
        } catch (Exception ex) {
            throw new NotFoundException("Failed to update customer with id: " + id + ". " + ex.getMessage());
        }
    }


    @Override
    public void deleteCustomer(Long idCustomer) {
    customerRepository.deleteById(idCustomer);
    }

    @Override
    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomerById(Long idCustomer) {
        Optional<Customer> customerOptional = customerRepository.findById(idCustomer);
        if (!customerOptional.isPresent()) {
            throw new EntityNotFoundException("Customer with id " + idCustomer + " not found");
        }
        return customerOptional.get();
    }

    @Override
    public List<Case> getCustomerCases(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));
        return customer.getCases();
    }
    @Override
    public long getTotalCasesByCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));
        List<Case> cases = caseRepository.findAllByCustomer(customer);
        return cases.size();
    }
    @Override
    public Customer getCustomerByCaseId(Long caseId) {
        return customerRepository.findByCases_IdCase(caseId);
    }


    @Override
    public List<Customer> getUserCustomers(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("customer not found"));
        return user.getCustomers();
    }
    @Override
    public List<Customer> getCustomersWithoutFolders()
    {
        return customerRepository.findByFolderIsNull();
    }
    @Override
    public List<Customer> getCustomersWithoutFolders(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        return user.getCustomers().stream()
                .filter(customer -> customer.getFolder() == null)
                .collect(Collectors.toList());
    }
}
