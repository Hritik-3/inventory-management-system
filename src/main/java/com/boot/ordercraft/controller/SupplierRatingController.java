package com.boot.ordercraft.controller;
 
import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
 
import com.boot.ordercraft.dto.SupplierRatingRequestDTO;
import com.boot.ordercraft.model.SupplierRating;
import com.boot.ordercraft.service.SupplierRatingService;
 
@RestController
@RequestMapping("/api/supplier-ratings")
@CrossOrigin("*")
public class SupplierRatingController {
 
    @Autowired
    private SupplierRatingService ratingService;
 
    @PostMapping("/add")
    public ResponseEntity<SupplierRating> addRating(@Validated @RequestBody SupplierRatingRequestDTO dto) {
        SupplierRating saved = ratingService.addRating(dto);
        return ResponseEntity.ok(saved);
    }
 
    @GetMapping("/all")
    public ResponseEntity<List<SupplierRating>> getAllRatings() {
        return ResponseEntity.ok(ratingService.getAllRatings());
    }
 
    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<SupplierRating>> getRatingsBySupplier(@PathVariable Long supplierId) {
        return ResponseEntity.ok(ratingService.getRatingsBySupplierId(supplierId));
    }
 
    @DeleteMapping("/{ratingId}")
    public ResponseEntity<Void> deleteRating(@PathVariable Long ratingId) {
        ratingService.deleteRating(ratingId);
        return ResponseEntity.noContent().build();
    }
}
 
 