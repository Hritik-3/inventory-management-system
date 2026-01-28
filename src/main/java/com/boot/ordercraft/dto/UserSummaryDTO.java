package com.boot.ordercraft.dto;

import java.time.LocalDateTime;

public class UserSummaryDTO {

    private String userId;
    private String fullName;
    private String email;
    private String mobile;
    private String roleName;
    private String accountStatus;
    private LocalDateTime lastLoginAt;

    public UserSummaryDTO(String userId, String fullName, String email, 
                          String mobile, String roleName, String accountStatus,
                          LocalDateTime lastLoginAt) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.mobile = mobile;
        this.roleName = roleName;
        this.accountStatus = accountStatus;
        this.lastLoginAt = lastLoginAt;
    }

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public LocalDateTime getLastLoginAt() {
		return lastLoginAt;
	}

	public void setLastLoginAt(LocalDateTime lastLoginAt) {
		this.lastLoginAt = lastLoginAt;
	}

    // Getters and Setters
    
    
}
