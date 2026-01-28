package com.boot.ordercraft.service;

import com.boot.ordercraft.model.Supplier;
import com.boot.ordercraft.repository.SuppliersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class SupplierContactService {

    private final SuppliersRepository supplierRepository;

    public SupplierContactService(SuppliersRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    // ---------- GET ALL ----------
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    // ---------- GET ONE ----------
    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id " + id));
    }

    // ---------- CREATE ----------
    public Supplier createSupplier(Supplier s) {

        // Basic validation
        if (s.getSuppliersName() == null || s.getSuppliersName().isBlank()) {
            throw new RuntimeException("Supplier name is required");
        }

        // Email validation & duplicate check
        if (s.getSuppliersEmail() != null && !s.getSuppliersEmail().isBlank()) {
            if (!isValidEmail(s.getSuppliersEmail()))
                throw new RuntimeException("Invalid email format");

            supplierRepository.findBySuppliersEmail(s.getSuppliersEmail())
                    .ifPresent(x -> { throw new RuntimeException("Email already exists for another supplier"); });
        }

        // Phone validation & duplicate check
        if (s.getSuppliersPhone() != null && !s.getSuppliersPhone().isBlank()) {
            if (!isValidPhone(s.getSuppliersPhone()))
                throw new RuntimeException("Invalid phone format");

            supplierRepository.findBySuppliersPhone(s.getSuppliersPhone())
                    .ifPresent(x -> { throw new RuntimeException("Phone number already exists for another supplier"); });
        }

        return supplierRepository.save(s);
    }

    // ---------- UPDATE ----------
    public Supplier updateSupplier(Long id, Supplier payload) {
        Supplier existing = getSupplierById(id);

        // Update name
        if (payload.getSuppliersName() != null && !payload.getSuppliersName().isBlank()) {
            existing.setSuppliersName(payload.getSuppliersName());
        }

        // Update email
        if (payload.getSuppliersEmail() != null) {
            String email = payload.getSuppliersEmail();
            if (!email.isBlank()) {
                if (!isValidEmail(email)) throw new RuntimeException("Invalid email format");

                Optional<Supplier> dup = supplierRepository.findBySuppliersEmail(email);
                if (dup.isPresent() && !dup.get().getSuppliersId().equals(id)) {
                    throw new RuntimeException("Email already exists for another supplier");
                }
                existing.setSuppliersEmail(email);
            } else {
                existing.setSuppliersEmail(null);
            }
        }

        // Update phone
        if (payload.getSuppliersPhone() != null) {
            String phone = payload.getSuppliersPhone();
            if (!phone.isBlank()) {
                if (!isValidPhone(phone)) throw new RuntimeException("Invalid phone format");

                Optional<Supplier> dup = supplierRepository.findBySuppliersPhone(phone);
                if (dup.isPresent() && !dup.get().getSuppliersId().equals(id)) {
                    throw new RuntimeException("Phone number already exists for another supplier");
                }
                existing.setSuppliersPhone(phone);
            } else {
                existing.setSuppliersPhone(null);
            }
        }

        // Update contact person
        if (payload.getSuppliersContactPerson() != null) {
            existing.setSuppliersContactPerson(
                    payload.getSuppliersContactPerson().isBlank() ? null : payload.getSuppliersContactPerson()
            );
        }

        // ---------- UPDATE ADDRESS (STRING NOW) ----------
        if (payload.getAddress() != null) {
            existing.setAddress(payload.getAddress().isBlank() ? null : payload.getAddress());
        }

        return supplierRepository.save(existing);
    }

    // ---------- DELETE ----------
    public void deleteSupplier(Long id) {
        Supplier s = getSupplierById(id);
        supplierRepository.delete(s);
    }

    // ---------- CONTACT UPDATE ----------
    public Supplier updateSupplierContact(Long id, String contactPerson, String email, String phone) {
        Supplier supplier = getSupplierById(id);

        if ((contactPerson == null || contactPerson.isBlank()) &&
            (email == null || email.isBlank()) &&
            (phone == null || phone.isBlank())) {
            throw new RuntimeException("At least one field must be provided");
        }

        if (email != null && !email.isBlank()) {
            if (!isValidEmail(email)) throw new RuntimeException("Invalid email format");

            Optional<Supplier> dup = supplierRepository.findBySuppliersEmail(email);
            if (dup.isPresent() && !dup.get().getSuppliersId().equals(id)) {
                throw new RuntimeException("Email already exists for another supplier");
            }
            supplier.setSuppliersEmail(email);
        }

        if (phone != null && !phone.isBlank()) {
            if (!isValidPhone(phone)) throw new RuntimeException("Invalid phone format");

            Optional<Supplier> dup = supplierRepository.findBySuppliersPhone(phone);
            if (dup.isPresent() && !dup.get().getSuppliersId().equals(id)) {
                throw new RuntimeException("Phone already exists for another supplier");
            }
            supplier.setSuppliersPhone(phone);
        }

        if (contactPerson != null && !contactPerson.isBlank()) {
            supplier.setSuppliersContactPerson(contactPerson);
        }

        return supplierRepository.save(supplier);
    }

    // ---------- DELETE CONTACT ----------
    public void deleteSupplierContact(Long id) {
        Supplier supplier = getSupplierById(id);
        supplier.setSuppliersContactPerson(null);
        supplier.setSuppliersEmail(null);
        supplier.setSuppliersPhone(null);
        supplierRepository.save(supplier);
    }

    // ---------- VALIDATION ----------
    private boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.compile(regex).matcher(email).matches();
    }

    private boolean isValidPhone(String phone) {
        String regex = "^\\d{10,15}$";
        return Pattern.compile(regex).matcher(phone).matches();
    }
}
