package com.boot.ordercraft.controller;

import com.boot.ordercraft.dto.ReturnOrderFetchDTO;
import com.boot.ordercraft.service.ReturnOrderFetchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/return-orders")
public class ReturnOrderFetchController {

    @Autowired
    private ReturnOrderFetchService returnOrderFetchService;

    /**
     * Fetch all return orders with their items
     * Example: GET http://localhost:8086/api/return-orders
     */
    @GetMapping("/fetch")
    public List<ReturnOrderFetchDTO> getAllReturnOrders() {
        return returnOrderFetchService.getAllReturnOrders();
    }
}
