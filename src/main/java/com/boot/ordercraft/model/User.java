package com.boot.ordercraft.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "USERS")
public class User implements Serializable {

    @Id
   
    @Column(name = "USERID")
    private String userId;

    @Column(name = "USERNAME")
    private String userName;

    @Column(name = "USERPASSWORD")
    private String userPassword;

    @Column(name = "USERFULLNAME")
    private String userFullName;

    @Column(name = "USEREMAIL")
    private String userEmail;

    @Column(name = "USERMOBILE")
    private String userMobile;

    @Column(name = "USERPROFILEIMG")
    private String userProfileImg;

    @ManyToOne
    @JoinColumn(name = "USERROLEID", referencedColumnName = "ROLEID")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "USERADDRESSID", referencedColumnName = "ADDRESSID")
    private Address address;

    
    
    @Column(name = "FAILED_LOGIN_ATTEMPTS", nullable = false,columnDefinition = "INT DEFAULT 0")
    private Integer failedLoginAttempts = 0;

    @Column(name = "ACCOUNT_LOCKED_AT")
    private LocalDateTime accountLockedAt;

    @Column(name = "LAST_LOGIN_AT")
    private LocalDateTime lastLoginAt;

    @Column(name = "SESSION_EXPIRES_AT")
    private LocalDateTime sessionExpiresAt;

    
    
    @Column(name="MUST_RESET")
    private Boolean mustReset;
    
    @Enumerated(EnumType.STRING)
    @Column(name="ACCOUNT_STATUS")
    private AccountStatus accountstatus;
    
    
    // Getters and Setters

    
    public AccountStatus getAccountstatus() {
		return accountstatus;
	}

	public void setAccountstatus(AccountStatus accountstatus) {
		this.accountstatus = accountstatus;
	}
    
	public Boolean getMustReset() {
		return mustReset;
	}

	

	public void setMustReset(Boolean mustReset) {
		this.mustReset = mustReset;
	}

	public Integer getFailedLoginAttempts() {
		return failedLoginAttempts;
	}

	public void setFailedLoginAttempts(Integer failedLoginAttempts) {
		this.failedLoginAttempts = failedLoginAttempts;
	}

	public LocalDateTime getAccountLockedAt() {
		return accountLockedAt;
	}

	public void setAccountLockedAt(LocalDateTime accountLockedAt) {
		this.accountLockedAt = accountLockedAt;
	}

	public LocalDateTime getLastLoginAt() {
		return lastLoginAt;
	}

	public void setLastLoginAt(LocalDateTime lastLoginAt) {
		this.lastLoginAt = lastLoginAt;
	}

	public LocalDateTime getSessionExpiresAt() {
		return sessionExpiresAt;
	}

	public void setSessionExpiresAt(LocalDateTime sessionExpiresAt) {
		this.sessionExpiresAt = sessionExpiresAt;
	}

	public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserProfileImg() {
        return userProfileImg;
    }

    public void setUserProfileImg(String userProfileImg) {
        this.userProfileImg = userProfileImg;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

	

	
}
