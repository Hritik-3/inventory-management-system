package com.boot.ordercraft.dto;

import java.util.List;

public class DashboardStatsDTO {

    private List<UserSummaryDTO> users;
    private List<RoleStatsDTO> roleStats;
    private List<AccountStatusStatsDTO> accountStatusStats;

    public DashboardStatsDTO(List<UserSummaryDTO> users,
                             List<RoleStatsDTO> roleStats,
                             List<AccountStatusStatsDTO> accountStatusStats) {
        this.users = users;
        this.roleStats = roleStats;
        this.accountStatusStats = accountStatusStats;
    }

	public List<UserSummaryDTO> getUsers() {
		return users;
	}

	public void setUsers(List<UserSummaryDTO> users) {
		this.users = users;
	}

	public List<RoleStatsDTO> getRoleStats() {
		return roleStats;
	}

	public void setRoleStats(List<RoleStatsDTO> roleStats) {
		this.roleStats = roleStats;
	}

	public List<AccountStatusStatsDTO> getAccountStatusStats() {
		return accountStatusStats;
	}

	public void setAccountStatusStats(List<AccountStatusStatsDTO> accountStatusStats) {
		this.accountStatusStats = accountStatusStats;
	}

    // Getters and Setters
    
    
}
