package com.boot.ordercraft.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boot.ordercraft.model.Supplier;

@Repository
public interface SuppliersRepository extends JpaRepository<Supplier, Long> {

    Optional<Supplier> findBySuppliersName(String suppliersName);

    // 🔹 Find supplier by email for duplicate check
    Optional<Supplier> findBySuppliersEmail(String suppliersEmail);

    // 🔹 Find supplier by phone for duplicate check
    Optional<Supplier> findBySuppliersPhone(String suppliersPhone);
}
