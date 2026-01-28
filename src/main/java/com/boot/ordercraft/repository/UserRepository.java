package com.boot.ordercraft.repository;

import com.boot.ordercraft.dto.AccountStatusStatsDTO;
import com.boot.ordercraft.dto.RoleStatsDTO;
import com.boot.ordercraft.model.AccountStatus;
import com.boot.ordercraft.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUserId(String userId);

    Optional<User> findByUserName(String username);

    @Query("SELECT u FROM User u WHERE u.userEmail = :email")
    Optional<User> findByEmail(String email);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.userEmail = :email")
    boolean existsByUserEmail(String email);

    @Query("SELECT new com.boot.ordercraft.dto.RoleStatsDTO(u.role.roleName, COUNT(u)) " +
            "FROM User u GROUP BY u.role.roleName")
     List<RoleStatsDTO> countUsersByRole();

     @Query("SELECT new com.boot.ordercraft.dto.AccountStatusStatsDTO(u.accountstatus, COUNT(u)) " +
            "FROM User u GROUP BY u.accountstatus")
     List<AccountStatusStatsDTO> countUsersByAccountStatus();

}
