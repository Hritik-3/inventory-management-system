package com.boot.ordercraft.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.*;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.*;

import com.boot.ordercraft.model.AccountStatus;
import com.boot.ordercraft.model.User;

import com.boot.ordercraft.security.JWTUtil;

import com.boot.ordercraft.service.UserService;

import com.boot.ordercraft.service.MailService.MailService;

import java.time.LocalDateTime;

import java.util.Map;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailService emailService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {

        String username = request.getUsername().trim().toLowerCase();
        String password = request.getPassword();

        Optional<User> optionalUser = userService.getByEmail(username);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        User user = optionalUser.get();

        // Check if account is already locked
        if (user.getAccountstatus() == AccountStatus.LOCKED ||
            user.getAccountstatus() == AccountStatus.ADMIN_LOCKED) {
            return ResponseEntity.status(403)
                    .body(Map.of("error", "Login failed: Account locked due to multiple failed attempts."));
        }

        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {

            if (user.getRole() != null && "ADMIN".equalsIgnoreCase(user.getRole().getRoleName())) {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials."));
            }

            int failedAttempts = user.getFailedLoginAttempts() + 1;
            user.setFailedLoginAttempts(failedAttempts);

            if (failedAttempts >= 3) {
                user.setAccountstatus(AccountStatus.LOCKED);       // ✅ Update status
                user.setAccountLockedAt(LocalDateTime.now());

                emailService.sendSimpleMessage(
                        user.getUserEmail(),
                        "Account Locked",
                        "Dear " + user.getUserFullName() + ",\n\n" +
                        "Your account has been locked due to multiple failed login attempts.\n" +
                        "Please contact support to unlock your account.\n\n" +
                        "Thank you."
                );
            }

            userService.saveUser(user);

            int attemptsLeft = Math.max(0, 3 - failedAttempts);
            String message = failedAttempts >= 3
                    ? "Account locked due to multiple failed attempts. Please contact support."
                    : "Invalid credentials. " + attemptsLeft + " attempt(s) remaining.";

            return ResponseEntity.status(401).body(Map.of(
                    "error", message,
                    "failedAttempts", failedAttempts,
                    "accountLocked", failedAttempts >= 3
            ));
        }

        // ✅ Successful login
        user.setFailedLoginAttempts(0);
        user.setLastLoginAt(LocalDateTime.now());
        user.setSessionExpiresAt(LocalDateTime.now().plusHours(1));
        userService.saveUser(user);

        String token = jwtUtil.generateToken(username);

        // ✅ Return AuthResponseDTO
        AuthResponseDTO response = new AuthResponseDTO();
        response.setToken(token);
        response.setUsername(user.getUserName() != null ? user.getUserName() : "");
        response.setRole(user.getRole() != null ? user.getRole().getRoleName() : "USER");
        response.setMustReset(user.getMustReset() != null ? user.getMustReset() : false);
        response.setUserId(user.getUserId());

        return ResponseEntity.ok(response);
    }

    // ✅ DTOs
    public static class AuthRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class AuthResponseDTO {
        private String token;
        private String username;
        private String role;
        private boolean mustReset;
        private String userId;

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

        public boolean isMustReset() { return mustReset; }
        public void setMustReset(boolean mustReset) { this.mustReset = mustReset; }

        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
    }
}
