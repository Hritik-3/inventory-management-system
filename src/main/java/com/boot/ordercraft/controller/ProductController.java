package com.boot.ordercraft.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.boot.ordercraft.dto.ProductStockView;
import com.boot.ordercraft.model.Product;
import com.boot.ordercraft.repository.ProductsRepository;
import com.boot.ordercraft.service.ProductService;


@RestController
@RequestMapping("/api/products")
public class ProductController {
	
	 @Autowired
	    private ProductsRepository productRepo;
	 @Autowired
	    private ProductService productService;

	    @GetMapping("/stocks")
	    public List<Product> getProductStocks() {
	        return productRepo.findAll(); // returns productName + productsQuantity
	    }
	    
	    
	    @GetMapping
	    
	    public ResponseEntity<List<Product>> getAll() {
	 
	        return ResponseEntity.ok(productService.getAllProducts());
	 
	    }
	    @GetMapping("/{id}")
	 
	    public ResponseEntity<Product> getById(@PathVariable Long id) {
	 
	        return ResponseEntity.ok(productService.getProductById(id));
	 
	    }
	    @PostMapping
	 
	    public ResponseEntity<Product> create(@RequestBody Product product) {
	 
	        return ResponseEntity.ok(productService.createProduct(product));
	 
	    }
	    @PutMapping("/{id}")
	 
	    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product product) {
	 
	        return ResponseEntity.ok(productService.updateProduct(id, product));
	 
	    }
	    @DeleteMapping("/{id}")
	 
	    public ResponseEntity<Void> delete(@PathVariable Long id) {
	 
	        productService.deleteProduct(id);
	 
	        return ResponseEntity.noContent().build();
	 
	    }
	    @GetMapping("/search")
	 
	    public ResponseEntity<List<Product>> search(@RequestParam String name) {
	 
	        return ResponseEntity.ok(productService.searchByName(name));
	 
	    }
	    // ================= Thresholds =================
	    @PutMapping("/{id}/thresholds")
	 
	    public ResponseEntity<Product> updateThresholds(
	 
	            @PathVariable Long id,
	 
	            @RequestBody Product thresholdsUpdate
	 
	    ) {
	 
	        return ResponseEntity.ok(productService.updateProductThresholds(
	 
	                id,
	 
	                thresholdsUpdate.getMinStockThreshold(),
	 
	                thresholdsUpdate.getMaxStockThreshold()
	 
	        ));
	 
	    }
	    // ================= Stock Management =================
	    @PutMapping("/{id}/stock")
	 
	    public ResponseEntity<String> updateStock(@PathVariable Long id, @RequestParam int change) {
	 
	        productService.updateProductStock(id, change);
	 
	        return ResponseEntity.ok("Stock updated successfully.");
	 
	    }
	    @GetMapping("/below-threshold")
	 
	    public ResponseEntity<List<Product>> getProductsBelowThreshold() {
	 
	        return ResponseEntity.ok(productService.getProductsBelowThreshold());
	 
	    }
	    @GetMapping("/{id}/restock-quantity")
	 
	    public ResponseEntity<Integer> getRestockQuantity(@PathVariable Long id) {
	 
	        return ResponseEntity.ok(productService.calculateRestockQuantity(id));
	 
	    }
	    @GetMapping("/category/{categoryId}")
	 
	    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Long categoryId) {
	 
	        return ResponseEntity.ok(productService.getProductsByCategory(categoryId));
	 
	    }
	    @GetMapping("/below-threshold/category/{categoryId}")
	 
	    public ResponseEntity<List<Product>> getProductsBelowThresholdByCategory(@PathVariable Long categoryId) {
	 
	        return ResponseEntity.ok(productService.getProductsBelowThresholdByCategory(categoryId));
	 
	    }
	    @GetMapping("/{id}/stock-info")
	 
	    public ResponseEntity<ProductStockView> getProductStockInfo(@PathVariable Long id) {
	 
	        return ResponseEntity.ok(productService.getProductStockInfo(id));
	 
	    }
	    
//	    @GetMapping("/stocks")
//	    public List<Product> getProductStocks() {
//	        return productRepo.findAll(); // returns productName + productsQuantity
//	    }
	    
	    
	    
	    public ProductController(ProductService productService) {
	        this.productService = productService;
	    }
	 
//	    @GetMapping
//	    public List<Product> getAll() {
//	        return productService.getAllProducts();
//	    }
//	 
//	    @GetMapping("/{id}")
//	    public Product getById(@PathVariable Long id) {
//	        return productService.getProductById(id);
//	    }
//	 
//	    @PostMapping
//	    public Product create(@RequestBody Product product) {
//	        return productService.createProduct(product);
//	    }
//	 
//	    @PutMapping("/{id}")
//	    public Product update(@PathVariable Long id, @RequestBody Product product) {
//	        return productService.updateProduct(id, product);
//	    }
//	 
//	    @DeleteMapping("/{id}")
//	    public void delete(@PathVariable Long id) {
//	        productService.deleteProduct(id);
//	    }
//	 
//	    @GetMapping("/search")
//	    public List<Product> search(@RequestParam String name) {
//	        return productService.searchByName(name);
//	    }

}
