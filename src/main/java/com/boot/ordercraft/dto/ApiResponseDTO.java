package com.boot.ordercraft.dto;
 
public class ApiResponseDTO {
    private String message;
    private Long returnOrderId;
    
 
    public ApiResponseDTO(String message, Long returnOrderId) {
        this.message = message;
    }
 
	public String getMessage() {
		return message;
	}
 
	public void setMessage(String message) {
		this.message = message;
	}
 
	public Long getReturnOrderId() {
		return returnOrderId;
	}
 
	public void setReturnOrderId(Long returnOrderId) {
		this.returnOrderId = returnOrderId;
	}
    
    
 
  
}
 
 
 