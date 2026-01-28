//productservice.java
package com.boot.ordercraft.service;

import com.boot.ordercraft.dto.ProductStockView;
 
import com.boot.ordercraft.model.Category;
 
import com.boot.ordercraft.model.Product;
 
import com.boot.ordercraft.repository.CategoriesRepository;
 
import com.boot.ordercraft.repository.ProductsRepository;
 
import org.springframework.stereotype.Service;

import java.util.List;
 
import java.util.stream.Collectors;

@Service
 
public class ProductService {

    private final ProductsRepository productsRepository;
 
    private final CategoriesRepository categoriesRepository;

    public ProductService(ProductsRepository productsRepository, CategoriesRepository categoriesRepository) {
 
        this.productsRepository = productsRepository;
 
        this.categoriesRepository = categoriesRepository;
 
    }

    // ================= CRUD =================

    public Product createProduct(Product product) {
 
        if (product.getCategory() != null && product.getCategory().getCategoriesId() != null) {
 
            Category category = categoriesRepository.findById(product.getCategory().getCategoriesId())
 
                    .orElseThrow(() -> new RuntimeException("Category not found with ID: " + product.getCategory().getCategoriesId()));
 
            product.setCategory(category);
 
        }
 
        return productsRepository.save(product);
 
    }

    public List<Product> getAllProducts() {
 
        return productsRepository.findAll();
 
    }

    public Product getProductById(Long id) {
 
        return productsRepository.findById(id)
 
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
 
    }

    public Product updateProduct(Long id, Product updatedProduct) {
 
        Product existing = getProductById(id);

        existing.setProductsName(updatedProduct.getProductsName());
 
        existing.setProductsDescription(updatedProduct.getProductsDescription());
 
        existing.setProductsUnitPrice(updatedProduct.getProductsUnitPrice());
 
        existing.setProductsQuantity(updatedProduct.getProductsQuantity());
 
        existing.setProductsImage(updatedProduct.getProductsImage());

        if (updatedProduct.getCategory() != null && updatedProduct.getCategory().getCategoriesId() != null) {
 
            Category category = categoriesRepository.findById(updatedProduct.getCategory().getCategoriesId())
 
                    .orElseThrow(() -> new RuntimeException("Category not found with ID: " + updatedProduct.getCategory().getCategoriesId()));
 
            existing.setCategory(category);
 
        }

        existing.setSupplier(updatedProduct.getSupplier());

        return productsRepository.save(existing);
 
    }

    public void deleteProduct(Long id) {
 
        if (!productsRepository.existsById(id)) {
 
            throw new RuntimeException("Product not found with ID: " + id);
 
        }
 
        productsRepository.deleteById(id);
 
    }

    public List<Product> searchByName(String name) {
 
        return productsRepository.searchByName(name);
 
    }

    public Product updateProductThresholds(Long id, Integer minThreshold, Integer maxThreshold) {
 
        Product existing = getProductById(id);

        if (minThreshold != null) {
 
            existing.setMinStockThreshold(minThreshold);
 
        }
 
        if (maxThreshold != null) {
 
            existing.setMaxStockThreshold(maxThreshold);
 
        }

        return productsRepository.save(existing);
 
    }

    // ================= Inventory Logic =================

    public void updateProductStock(Long id, int change) {
 
        Product product = getProductById(id);
 
        int newQty = product.getProductsQuantity() + change;

        if (newQty < 0) {
 
            throw new RuntimeException("Stock cannot go negative!");
 
        }

        product.setProductsQuantity(newQty);
 
        productsRepository.save(product);
 
    }

    public List<Product> getProductsBelowThreshold() {
 
        return productsRepository.findAll()
 
                .stream()
 
                .filter(p -> p.getMinStockThreshold() != null &&
 
                        p.getProductsQuantity() < p.getMinStockThreshold())
 
                .collect(Collectors.toList());
 
    }

    public int calculateRestockQuantity(Long id) {
 
        Product product = getProductById(id);
 
        if (product.getMaxStockThreshold() == null) {
 
            return 0;
 
        }
 
        return Math.max(0, product.getMaxStockThreshold() - product.getProductsQuantity());
 
    }

    public List<Product> getProductsByCategory(Long categoryId) {
 
        Category category = categoriesRepository.findById(categoryId)
 
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId));
 
        return productsRepository.findAll()
 
                .stream()
 
                .filter(p -> p.getCategory() != null && p.getCategory().equals(category))
 
                .collect(Collectors.toList());
 
    }

    public List<Product> getProductsBelowThresholdByCategory(Long categoryId) {
 
        return getProductsByCategory(categoryId)
 
                .stream()
 
                .filter(p -> p.getMinStockThreshold() != null &&
 
                        p.getProductsQuantity() < p.getMinStockThreshold())
 
                .collect(Collectors.toList());
 
    }

    public ProductStockView getProductStockInfo(Long id) {
 
        Product product = getProductById(id);

        int qty = product.getProductsQuantity();
 
        int min = product.getMinStockThreshold() != null ? product.getMinStockThreshold() : 0;
 
        int max = product.getMaxStockThreshold() != null ? product.getMaxStockThreshold() : 0;

        String alert;
 
        if (product.getMinStockThreshold() == null && product.getMaxStockThreshold() == null) {
 
            alert = "No thresholds set";
 
        } else if (product.getMinStockThreshold() != null && qty < product.getMinStockThreshold()) {
 
            alert = "LOW";
 
        } else if (product.getMaxStockThreshold() != null && product.getMaxStockThreshold() > 0 && qty > product.getMaxStockThreshold()) {
 
            alert = "OVERSTOCK";
 
        } else {
 
            alert = "OK";
 
        }

        return new ProductStockView(
 
                product.getProductsId(),
 
                product.getProductsName(),
 
                qty,
 
                min,
 
                max,
 
                alert
 
        );
 
    }
    
 
    // CREATE product (with category)
//    public Product createProduct(Product product) {
//        if (product.getCategory() != null && product.getCategory().getCategoriesId() != null) {
//            Category category = categoriesRepository.findById(product.getCategory().getCategoriesId())
//                    .orElseThrow(() -> new RuntimeException("Category not found with ID: " + product.getCategory().getCategoriesId()));
//            product.setCategory(category);
//        }
//        return productsRepository.save(product);
//    }
 
    // READ all products
//    public List<Product> getAllProducts() {
//        return productsRepository.findAll();
//    }
// 
//    // READ product by ID
//    public Product getProductById(Long id) {
//        return productsRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
//    }
 
    // UPDATE product (with category)
//    public Product updateProduct(Long id, Product updatedProduct) {
//        Product existing = getProductById(id);
// 
//        existing.setProductsName(updatedProduct.getProductsName());
//        existing.setProductsDescription(updatedProduct.getProductsDescription());
//        existing.setProductsUnitPrice(updatedProduct.getProductsUnitPrice());
//        existing.setProductsQuantity(updatedProduct.getProductsQuantity());
//        existing.setProductsImage(updatedProduct.getProductsImage());
// 
//        if (updatedProduct.getCategory() != null && updatedProduct.getCategory().getCategoriesId() != null) {
//            Category category = categoriesRepository.findById(updatedProduct.getCategory().getCategoriesId())
//                    .orElseThrow(() -> new RuntimeException("Category not found with ID: " + updatedProduct.getCategory().getCategoriesId()));
//            existing.setCategory(category);
//        }
// 
//        existing.setSupplier(updatedProduct.getSupplier()); // if supplier management exists
// 
//        return productsRepository.save(existing);
//    }
 
    // DELETE product
//    public void deleteProduct(Long id) {
//        if (!productsRepository.existsById(id)) {
//            throw new RuntimeException("Product not found with ID: " + id);
//        }
//        productsRepository.deleteById(id);
//    }
 
//    // SEARCH products by name
//    public List<Product> searchByName(String name) {
//        return productsRepository.searchByName(name);
//    }
 
}
 
 