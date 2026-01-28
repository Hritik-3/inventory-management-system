package com.boot.ordercraft.model;


import jakarta.persistence.*;

@Entity
@Table(name = "ADDRESS")
public class Address {
	    @Id
	    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_seq")
	    @SequenceGenerator(
	        name = "address_seq",       // JPA sequence generator name
	        sequenceName = "ADDRESS_SEQ", // Actual DB sequence name
	        allocationSize = 1,         // Increment step
	        initialValue = 1             // Start from 1
	    )
	    
	    @Column(name = "ADDRESSID")
	    private Long addressId;

	    @Column(name = "ADDRESSSTREET")
	    private String addressStreet;

	    @Column(name = "ADDRESSCITY")
	    private String addressCity;

	    @Column(name = "ADDRESSSTATE")
	    private String addressState;

	    @Column(name = "ADDRESSPOSTALCODE")
	    private String addressPostalCode;

	    @Column(name = "ADDRESSCOUNTRY")
	    private String addressCountry;

    // Constructors, getters and setters
    public Address() {}
    public Address(Long addressId, String addressStreet, String addressCity, String addressState,
                   String addressPostalCode, String addressCountry) {
        this.addressId = addressId;
        this.addressStreet = addressStreet;
        this.addressCity = addressCity;
        this.addressState = addressState;
        this.addressPostalCode = addressPostalCode;
        this.addressCountry = addressCountry;
    }

    public Long getAddressId() { return addressId; }
    public void setAddressId(Long addressId) { this.addressId = addressId; }
    public String getAddressStreet() { return addressStreet; }
    public void setAddressStreet(String addressStreet) { this.addressStreet = addressStreet; }
    public String getAddressCity() { return addressCity; }
    public void setAddressCity(String addressCity) { this.addressCity = addressCity; }
    public String getAddressState() { return addressState; }
    public void setAddressState(String addressState) { this.addressState = addressState; }
    public String getAddressPostalCode() { return addressPostalCode; }
    public void setAddressPostalCode(String addressPostalCode) { this.addressPostalCode = addressPostalCode; }
    public String getAddressCountry() { return addressCountry; }
    public void setAddressCountry(String addressCountry) { this.addressCountry = addressCountry; }
}
