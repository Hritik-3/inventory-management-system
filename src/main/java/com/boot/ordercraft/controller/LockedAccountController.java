package com.boot.ordercraft.controller;

import com.boot.ordercraft.model.User;
import com.boot.ordercraft.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/locked-accounts")
public class LockedAccountController {

    @Autowired
    private UserService userService;

    // Fetch all locked accounts
    @GetMapping
    public ResponseEntity<List<User>> getAllLockedAccounts() {
        List<User> lockedUsers = userService.getLockedAccounts();
        return ResponseEntity.ok(lockedUsers);
    }

    // Unlock all locked accounts
    @PutMapping("/unlock-all")
    public ResponseEntity<Map<String, String>> unlockAllAccounts() {
        int unlockedCount = userService.unlockAllLockedAccounts();
        return ResponseEntity.ok(Map.of("message", unlockedCount + " accounts unlocked successfully."));
    }
}
