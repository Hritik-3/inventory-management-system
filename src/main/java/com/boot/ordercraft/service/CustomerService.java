package com.boot.ordercraft.service;

import com.boot.ordercraft.dto.CustomerDto;
import com.boot.ordercraft.model.Customer;
import com.boot.ordercraft.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    // Add new customer
    public CustomerDto addCustomer(CustomerDto dto) {
        // ✅ Check if customer exists by email using Oracle-compatible query
        Optional<Customer> existingCustomer = customerRepository.findByEmail(dto.getEmail());
        if (existingCustomer.isPresent()) {
            throw new RuntimeException("Customer already exists with this email");
        }

        // Map DTO → Entity
        Customer customer = mapToEntity(dto);
        
        customer.setActive(true);

        // Save to DB
        Customer saved = customerRepository.save(customer);

        // Map Entity → DTO and return
        return mapToDto(saved);
    }

    // Get all customers
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Get customer by ID
    public CustomerDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return mapToDto(customer);
    }

    // Update customer
    public CustomerDto updateCustomer(Long id, CustomerDto dto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.setName(dto.getName());
        customer.setPhone(dto.getPhone());
        customer.setAddress(dto.getAddress());
        customer.setActive(dto.isActive());

        return mapToDto(customerRepository.save(customer));
    }

    // Soft delete customer
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.setActive(false); // soft delete
        customerRepository.save(customer);
    }

    // DTO → Entity mapping
    private Customer mapToEntity(CustomerDto dto) {
        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setAddress(dto.getAddress());
        customer.setActive(dto.isActive());
        return customer;
    }

    // Entity → DTO mapping
    private CustomerDto mapToDto(Customer customer) {
        CustomerDto dto = new CustomerDto();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        dto.setPhone(customer.getPhone());
        dto.setAddress(customer.getAddress());
        dto.setActive(customer.isActive());
        return dto;
    }
}
