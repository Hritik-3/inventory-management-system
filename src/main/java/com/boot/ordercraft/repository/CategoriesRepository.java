package com.boot.ordercraft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boot.ordercraft.model.Category;

@Repository
public interface CategoriesRepository extends JpaRepository<Category, Long> {}
