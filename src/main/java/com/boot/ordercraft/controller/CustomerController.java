package com.boot.ordercraft.controller;
 
import com.boot.ordercraft.dto.CustomerDto;
 
import com.boot.ordercraft.service.CustomerService;
 
import org.springframework.beans.factory.annotation.Autowired;
 
import org.springframework.http.ResponseEntity;
 
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
 
@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "http://localhost:4200") 
public class CustomerController {

    @Autowired
    private CustomerService customerService;
  
 
    @PostMapping("/create/customer")
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto dto) {
        return ResponseEntity.ok(customerService.addCustomer(dto));
    }

 
    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAll() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }
 
    @GetMapping("/id/{id}")
    public ResponseEntity<CustomerDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }
    
 
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> update(@PathVariable Long id, @RequestBody CustomerDto dto) {
        return ResponseEntity.ok(customerService.updateCustomer(id, dto));
    }
 
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok("Customer deactivated successfully");
    }
 
}
 
 
 