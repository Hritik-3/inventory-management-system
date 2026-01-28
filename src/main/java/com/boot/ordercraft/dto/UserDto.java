package com.boot.ordercraft.dto;

import com.boot.ordercraft.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String userId;
    private String userName;
    private String userEmail;
    private boolean accountLocked;

    

	public static UserDto fromEntity(User user) {
        return new UserDto(
            user.getUserId(),
            user.getUserName(),
            user.getUserEmail(),
            user.getAccountLockedAt() != null
        );
    }



	public UserDto(String userId, String userName, String userEmail, boolean accountLocked) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userEmail = userEmail;
		this.accountLocked = accountLocked;
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



	public String getUserEmail() {
		return userEmail;
	}



	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}



	public boolean isAccountLocked() {
		return accountLocked;
	}



	public void setAccountLocked(boolean accountLocked) {
		this.accountLocked = accountLocked;
	}
	
	
	
}
