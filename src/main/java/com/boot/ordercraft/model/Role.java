package com.boot.ordercraft.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ROLES")
public class Role {

    @Id
    
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq")
      @SequenceGenerator(
          name = "role_seq",
          sequenceName = "ROLE_SEQ",
          allocationSize = 1,
          initialValue = 1
      )
    
    @Column(name = "ROLEID", nullable = false) // Make non-nullable
    private Long roleId;

    @Column(name = "ROLENAME", nullable = false) // Make non-nullable
    private String roleName;

    // Constructors
    public Role() {}

    public Role(Long roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    // Getters and setters
    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
