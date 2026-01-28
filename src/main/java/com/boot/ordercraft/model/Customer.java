package com.boot.ordercraft.model;
 
import jakarta.persistence.*;
 
import java.util.List;
 
@Entity
 
@Table(name = "CUSTOMERS")
 
public class Customer {
 
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_seq")
	@SequenceGenerator(
	    name = "customer_seq",
	    sequenceName = "CUSTOMER_SEQ",
	    allocationSize = 1
	)
 
    @Column(name = "CUST_ID")
 
    private Long id;
 
    @Column(name = "CUST_NAME", nullable = false)
 
    private String name;
 
    @Column(name = "CUST_EMAIL", nullable = false, unique = true)
 
    private String email;
 
    @Column(name = "CUST_PHONE", nullable = false)
 
    private String phone;
 
    @Column(name = "CUST_ADDRESS", nullable = false)
 
    private String address;
 
    @Column(name = "IS_ACTIVE")
 
    private boolean active = true;
 
    // If you're mapping customer to purchase orders
 
    @ManyToMany(mappedBy = "customer", cascade = CascadeType.ALL)
 
    private List<PurchaseOrder> purchaseOrders;
 
    
 
    public Customer() {}
 
    public Customer(Long id, String name, String email, String phone, String address, boolean active) {
 
        this.id = id;
 
        this.name = name;
 
        this.email = email;
 
        this.phone = phone;
 
        this.address = address;
 
        this.active = active;
 
    }
 
    // Getters and Setters
 
    public Long getId() {
 
        return id;
 
    }
 
    public void setId(Long id) {
 
        this.id = id;
 
    }
 
    public String getName() {
 
        return name;
 
    }
 
    public void setName(String name) {
 
        this.name = name;
 
    }
 
    public String getEmail() {
 
        return email;
 
    }
 
    public void setEmail(String email) {
 
        this.email = email;
 
    }
 
    public String getPhone() {
 
        return phone;
 
    }
 
    public void setPhone(String phone) {
 
        this.phone = phone;
 
    }
 
    public String getAddress() {
 
        return address;
 
    }
 
    public void setAddress(String address) {
 
        this.address = address;
 
    }
 
    public boolean isActive() {
 
        return active;
 
    }
 
    public void setActive(boolean active) {
 
        this.active = active;
 
    }
 
    public List<PurchaseOrder> getPurchaseOrders() {
 
        return purchaseOrders;
 
    }
 
    public void setPurchaseOrders(List<PurchaseOrder> purchaseOrders) {

        this.purchaseOrders = purchaseOrders; 
    }

}
 
 
 