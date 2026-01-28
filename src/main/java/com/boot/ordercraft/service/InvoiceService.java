package com.boot.ordercraft.service;
 
import com.boot.ordercraft.dto.InvoiceResponseDTO;
 
public interface InvoiceService {
    InvoiceResponseDTO generateInvoice(Long orderId);
}
 
 