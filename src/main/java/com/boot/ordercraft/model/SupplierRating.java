package com.boot.ordercraft.model;

import jakarta.persistence.*;

import java.time.LocalDate;
 
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
 
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

@Entity

@Table(name = "SUPPLIER_RATING")

public class SupplierRating {

    @Id

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rating_seq")

    @SequenceGenerator(

        name = "rating_seq",

        sequenceName = "SUPPLIER_RATING_SEQ",

        allocationSize = 1,

        initialValue = 1

    )

    @Column(name = "RATING_ID")

    private Long ratingId;

    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "SUPPLIER_ID", referencedColumnName = "SUPPLIERSID", nullable = false)

    private Supplier supplier;

    @Column(name = "RATING_VALUE", nullable = false)

    private Double ratingValue;

    @Column(name = "COMMENTS")

    private String comments;

    @Column(name = "RATING_DATE")

    private LocalDate ratingDate = LocalDate.now();

    // Constructors

    public SupplierRating() {}

    public SupplierRating(Supplier supplier, Double ratingValue, String comments) {

        this.supplier = supplier;

        this.ratingValue = ratingValue;

        this.comments = comments;

        this.ratingDate = LocalDate.now();

    }

    // Getters and setters

    public Long getRatingId() { return ratingId; }

    public void setRatingId(Long ratingId) { this.ratingId = ratingId; }

    public Supplier getSupplier() { return supplier; }

    public void setSupplier(Supplier supplier) { this.supplier = supplier; }

    public Double getRatingValue() { return ratingValue; }

    public void setRatingValue(Double ratingValue) { this.ratingValue = ratingValue; }

    public String getComments() { return comments; }

    public void setComments(String comments) { this.comments = comments; }

    public LocalDate getRatingDate() { return ratingDate; }

    public void setRatingDate(LocalDate ratingDate) { this.ratingDate = ratingDate; }

}

 