package com.boot.ordercraft.service;

import com.boot.ordercraft.dto.*;
import com.boot.ordercraft.model.User;
import com.boot.ordercraft.repository.UserRepository;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminDashboardService {

    private final UserRepository userRepository;

    public AdminDashboardService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public DashboardStatsDTO getDashboardStats() {

        // Fetch all users & map to DTO
        List<UserSummaryDTO> users = userRepository.findAll()
                .stream()
                .map(u -> new UserSummaryDTO(
                        u.getUserId(),
                        u.getUserFullName(),
                        u.getUserEmail(),
                        u.getUserMobile(),
                        u.getRole().getRoleName(),
                        (u.getAccountstatus() == null ? "UNKNOWN" : u.getAccountstatus().name()),
                        u.getLastLoginAt()
                ))
                .collect(Collectors.toList());

        // Count users grouped by roles
        List<RoleStatsDTO> roleStats = userRepository.countUsersByRole();

        // Count users grouped by account status
        List<AccountStatusStatsDTO> accountStatusStats = userRepository.countUsersByAccountStatus();

        return new DashboardStatsDTO(users, roleStats, accountStatusStats);
    }
}
