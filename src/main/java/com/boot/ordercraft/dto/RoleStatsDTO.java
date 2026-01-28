package com.boot.ordercraft.dto;

public class RoleStatsDTO {

    private String roleName;
    private Long count;

    public RoleStatsDTO(String roleName, Long count) {
        this.roleName = roleName;
        this.count = count;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
