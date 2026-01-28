package com.boot.ordercraft.dto;

import java.time.LocalDate;

import com.boot.ordercraft.model.SupplierPayment;

//SupplierPaymentDto newly created

public class SupplierPaymentDTO {
    private Long paymentId;
    private String supplierName;
    private String supplierEmail;
    private Double amountPaid;
    private LocalDate paymentDate;
    private String paymentMode;
    private String paymentStatus;
    private String referenceNo;
    private String remarks;
    
    

    public Long getPaymentId() {
		return paymentId;
	}



	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}



	public String getSupplierName() {
		return supplierName;
	}



	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}



	public String getSupplierEmail() {
		return supplierEmail;
	}



	public void setSupplierEmail(String supplierEmail) {
		this.supplierEmail = supplierEmail;
	}



	public Double getAmountPaid() {
		return amountPaid;
	}



	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}



	public LocalDate getPaymentDate() {
		return paymentDate;
	}



	public void setPaymentDate(LocalDate paymentDate) {
		this.paymentDate = paymentDate;
	}



	public String getPaymentMode() {
		return paymentMode;
	}



	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}



	public String getPaymentStatus() {
		return paymentStatus;
	}



	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}



	public String getReferenceNo() {
		return referenceNo;
	}



	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}



	public String getRemarks() {
		return remarks;
	}



	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}



	public SupplierPaymentDTO(SupplierPayment payment) {
        this.paymentId = payment.getPaymentId();
        this.supplierName = payment.getSupplier().getSuppliersName();
        this.supplierEmail = payment.getSupplier().getSuppliersEmail();
        this.amountPaid = payment.getAmountPaid();
        this.paymentDate = payment.getPaymentDate();
        this.paymentMode = payment.getPaymentMode();
        this.paymentStatus = payment.getPaymentStatus();
        this.referenceNo = payment.getReferenceNo();
        this.remarks = payment.getRemarks();
    }
}
