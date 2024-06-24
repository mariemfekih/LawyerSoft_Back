package com.example.gestion_user.controllers;

import com.example.gestion_user.entities.Auxiliary;
import com.example.gestion_user.entities.Case;
import com.example.gestion_user.entities.Customer;
import com.example.gestion_user.models.request.CustomerDto;
import com.example.gestion_user.services.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/Customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;
    @PostMapping("/{userId}")
    public ResponseEntity<Customer> addCustomer(@RequestBody CustomerDto a,@PathVariable Long userId) {
        Customer addedCustomer = customerService.addCustomer(a,userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedCustomer);
    }

    /*@PostMapping("/{caseId}")
    public ResponseEntity<Customer> addCustomer(@RequestBody CustomerDto a, @PathVariable Long caseId) {
        Customer addedCustomer = customerService.addCustomer(a,caseId);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedCustomer);
    }*/

    @PutMapping("/{idCustomer}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long idCustomer, @RequestBody CustomerDto updatedCustomer) {
        Customer existingCustomer = customerService.getCustomerById(idCustomer);

        if (existingCustomer == null) {
            return ResponseEntity.notFound().build();
        }
        Customer updatedEntity = customerService.updateCustomer(idCustomer,updatedCustomer);
        return ResponseEntity.ok(updatedEntity);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{idCustomer}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long idCustomer) {
        customerService.deleteCustomer(idCustomer);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getCustomers() {
        List<Customer> customers = customerService.getCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        Customer a = customerService.getCustomerById(id);
        if (a != null) {
            return ResponseEntity.ok(a);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{customerId}/cases")
    public ResponseEntity<List<Case>> getCustomerCases(@PathVariable Long customerId) {
        List<Case> cases = customerService.getCustomerCases(customerId);
        return ResponseEntity.ok(cases);
    }
    @GetMapping("/case/{caseId}")
    public ResponseEntity<Customer> getCustomerByCaseId(@PathVariable Long caseId) {
        Customer customer = customerService.getCustomerByCaseId(caseId);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Customer>> getUserCustomers(@PathVariable Long userId) {
        List<Customer> customers = customerService.getUserCustomers(userId);
        // Filter out duplicate customers based on idCustomer
        Set<Long> seenCustomerIds = new HashSet<>();
        customers = customers.stream()
                .filter(customer -> seenCustomerIds.add(customer.getIdCustomer()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(customers);
    }
    @GetMapping("/withoutFolders")
    public List<Customer> getCustomersWithoutFolders() {
        return customerService.getCustomersWithoutFolders();
    }

    @GetMapping("/users/{userId}/customers-without-folders")
    public List<Customer> getCustomersWithoutFolders(@PathVariable Long userId) {
        return customerService.getCustomersWithoutFolders(userId);
    }

}
