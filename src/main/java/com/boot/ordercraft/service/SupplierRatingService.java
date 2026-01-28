package com.boot.ordercraft.service;
 
import java.time.LocalDate;

import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
 
import com.boot.ordercraft.dto.SupplierRatingRequestDTO;

import com.boot.ordercraft.model.Supplier;

import com.boot.ordercraft.model.SupplierRating;

import com.boot.ordercraft.repository.SupplierRatingRepository;

import com.boot.ordercraft.repository.SuppliersRepository;
 
import jakarta.persistence.EntityNotFoundException;
 
@Service

public class SupplierRatingService {

//with latest methods

    @Autowired

    private SupplierRatingRepository ratingRepository;
 
    @Autowired

    private SuppliersRepository suppliersRepository;
 
    /**

     * 🔹 Add a new rating for a supplier

     */

    public SupplierRating addRating(SupplierRatingRequestDTO dto) {

        Supplier supplier = suppliersRepository.findById(dto.getSupplierId())

                .orElseThrow(() -> new EntityNotFoundException("Supplier not found with ID: " + dto.getSupplierId()));
 
        SupplierRating rating = new SupplierRating();

        rating.setSupplier(supplier);

        rating.setRatingValue(dto.getRatingValue());

        rating.setComments(dto.getComments());

        rating.setRatingDate(LocalDate.now()); // ✅ matches your model field type
 
        return ratingRepository.save(rating);

    }
 
    /**

     * 🔹 Get all ratings

     */

    public List<SupplierRating> getAllRatings() {

        return ratingRepository.findAll();

    }
 
    /**

     * 🔹 Get all ratings for a specific supplier

     */

    public List<SupplierRating> getRatingsBySupplierId(Long supplierId) {

        return ratingRepository.findBySupplier_SuppliersId(supplierId);

    }
 
    /**

     * 🔹 Get the latest rating for a specific supplier (Oracle compatible)

     */

    public SupplierRating getLatestRatingBySupplierId(Long supplierId) {

        return ratingRepository.findLatestBySupplierId(supplierId);

    }
 
    /**

     * 🔹 Delete a rating by its ID

     */

    public void deleteRating(Long ratingId) {

        if (!ratingRepository.existsById(ratingId)) {

            throw new EntityNotFoundException("Rating not found with ID: " + ratingId);

        }

        ratingRepository.deleteById(ratingId);

    }

}

 