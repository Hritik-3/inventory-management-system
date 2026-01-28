package com.boot.ordercraft.dto;

public class SupplierDTO {
    private Long id;
    private String name;

    public SupplierDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    public Long getId() { return id; }
    public String getName() { return name; }
}
