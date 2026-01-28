package com.boot.ordercraft.controller;
 
 
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
 
import com.boot.ordercraft.repository.RoleRepository;
import com.boot.ordercraft.repository.SuppliersRepository;
import com.boot.ordercraft.repository.UserRepository;
import com.boot.ordercraft.service.SecuredPasswordGenerator;
import com.boot.ordercraft.service.UserService;
import com.boot.ordercraft.service.MailService.MailService;
import com.boot.ordercraft.util.OtpStorage;
import com.boot.ordercraft.util.OtpUtil;
import com.boot.ordercraft.dto.UserDto;
import com.boot.ordercraft.dto.UserProfileUpdateDTO;
import com.boot.ordercraft.model.AccountStatus;
import com.boot.ordercraft.model.Role;
import com.boot.ordercraft.model.Supplier;
import com.boot.ordercraft.model.User;
 
 
@CrossOrigin(origins ="http://localhost:4200")
@RestController
@RequestMapping("/api/users")
public class UserController {
 
    @Autowired
    private UserService userService;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private MailService emailService;
    
    @Autowired
    private OtpUtil otpUtil;
    
    @Autowired
    private OtpStorage otpStorage;
    
    @Autowired
    private RoleRepository roleRepo;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private SuppliersRepository supplierrepo;
    
    
    
    // To register User
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }
    
 
 
    //To update User
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User updatedUser) {
        User user = userService.updateUser(id, updatedUser);
        return ResponseEntity.ok(user);
    }
 
 
    //To Fetch User details
    @GetMapping("/{id}")
    public User getUser(@PathVariable String id) {
        return userService.getUserById(id);
    }
    
    
    //To delete/deactivate User
    @PutMapping("/deactivate-account")
    public ResponseEntity<?> deactivateUser(@RequestParam String email) {
        Optional<User> opt = userRepository.findByEmail(email);
        if (opt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not found."));
        }
 
        User user = opt.get();
        user.setAccountstatus(AccountStatus.INACTIVE);
        user.setAccountLockedAt(null);
        user.setFailedLoginAttempts(0);
        userRepository.save(user);
 
        emailService.sendSimpleMessage(
            user.getUserEmail(),
            "Account Deactivated",
            "Dear " + user.getUserFullName() + ",\n\n" +
            "Your account has been deactivated by the administrator.\n\n" +
            "Regards."
        );
 
        return ResponseEntity.ok(Map.of("message", "User account deactivated."));
    }
 
 
// Update User Role
    @PutMapping("/role/{userId}")
    public ResponseEntity<Map<String, String>> updateUserRole(
            @PathVariable String userId,
            @RequestBody Role newRole) {
 
        Map<String, String> response = new HashMap<>();
 
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            response.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
 
        Optional<Role> optionalRole = roleRepository.findByRoleName(newRole.getRoleName());
        if (optionalRole.isEmpty()) {
            response.put("message", "Role not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
 
        User user = optionalUser.get();
        user.setRole(optionalRole.get());
        userRepository.save(user);
 
        response.put("message", "User role updated successfully");
        return ResponseEntity.ok(response);
    }
 
    
    
    //Reset password
    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request, Principal principal) {
        String username = principal.getName();
 
        System.out.println("Authenticated principal name: " + username);
 
        Optional<User> userOpt = userService.getUserByUsername(username);
        if (userOpt.isEmpty()) {
            // Try email lookup too
            userOpt = userService.getByEmail(username);
        }
 
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
 
        User user = userOpt.get();
 
        // Compare the current password provided with the stored hash
        boolean matches = passwordEncoder.matches(request.getCurrentPassword(), user.getUserPassword());
        if (!matches) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Current password is incorrect.");
        }
        
        
        // Check if the new password is the same as the old one
        if (passwordEncoder.matches(request.getNewPassword(), user.getUserPassword())) {
            return ResponseEntity.badRequest().body( "New password must be different from the old one.");
        }
 
        
      
 
        user.setUserPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setMustReset(false);
        userService.saveUser(user);
 
        return ResponseEntity.ok("Password reset successfully.");
    }
    
    
    //Send OTP
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        emailService.generateAndStoreOtp(email);
        return ResponseEntity.ok("OTP sent to email.");
    }
 
    //Verify OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
 
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body("Email is required.");
        }
        if (otp == null || otp.isBlank()) {
            return ResponseEntity.badRequest().body("OTP is required.");
        }
 
        boolean isValid = emailService.verifyOtp(email, otp);
 
        if (isValid) {
            return ResponseEntity.ok("OTP verified.");
        } else {
            return ResponseEntity.badRequest().body("Invalid OTP.");
        }
    }
 
    //reset password with OTP
    @PostMapping("/reset-password-with-otp")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        String newPassword = request.get("newPassword");
 
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is required."));
        }
        if (otp == null || otp.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "OTP is required."));
        }
        if (newPassword == null || newPassword.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "New password is required."));
        }
 
        // Check if user exists
        Optional<User> opt = userRepository.findByEmail(email);
        if (opt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "User with this email not found."));
        }
 
        User user = opt.get();
 
        // Disallow resetting ADMIN passwords via this endpoint
        if ("ADMIN".equalsIgnoreCase(user.getRole().getRoleName())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Admin password cannot be reset via this method."));
        }
 
        // Validate OTP
        if (!emailService.verifyOtp(email, otp)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid OTP."));
        }
 
        // Check if the new password is the same as the old one
        if (passwordEncoder.matches(newPassword, user.getUserPassword())) {
            return ResponseEntity.badRequest().body(Map.of("message", "New password must be different from the old one."));
        }
 
        // Save the new password
        userService.updatePassword(email, newPassword);
 
        return ResponseEntity.ok(Map.of("message", "Password reset successfully."));
    }
 
 
    //Forgot password
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        Optional<User> optionalUser = userService.getByEmail(email);
 
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No user found with that email.");
        }
        
    
       
    		   
        User user = optionalUser.get();
        
        AccountStatus status = user.getAccountstatus(); // e.g., "ACTIVE", "LOCKED", "ADMIN_LOCKED", "TEMP_LOCKED"
        if (status != AccountStatus.ACTIVE) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Your account is currently '" + status + "'. Password reset is not allowed.");
        }
 
 
        // Generate temporary random password
        String tempPassword =  SecuredPasswordGenerator.generatePassword();
 
        
       
        
        // Encode and save it
        user.setUserPassword(passwordEncoder.encode(tempPassword));
        user.setMustReset(true);
        userService.saveUser(user);
 
        // TODO: send email
        emailService.sendEmail(
            user.getUserEmail(),
            "Password Reset Request",
            "Your temporary password is: " + tempPassword + "\nPlease log in and reset your password."
        );
 
        return ResponseEntity.ok("Temporary password sent to your email.");
    }
 
 
    //Get Roles
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleRepo.findAll();
        return ResponseEntity.ok(roles);
    }
    
    //Get all users
    @GetMapping("/getallusers")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        
        return ResponseEntity.ok(users);
    }
 
    //Search Users
    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> searchUsers(@RequestParam String query) {
        Optional<User> users = userService.searchByEmailOrName(query);
        List<UserDto> result = users.stream().map(UserDto::fromEntity).toList();
        return ResponseEntity.ok(result);
    }
    
    
    //Get User by Email
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.ok(userOpt.get());
    }
 
 
    //Unlock account
    @PutMapping("/unlock-account")
    public ResponseEntity<?> unlockAccount(@RequestParam String email) {
        Optional<User> opt = userRepository.findByEmail(email);
        if (opt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not found."));
        }
        User user = opt.get();
        
        if (user.getAccountLockedAt() == null) {
        	if(user.getAccountstatus()==AccountStatus.ACTIVE) {
            return ResponseEntity.ok("Account is not locked.");
        	}
        }
 
        
        user.setAccountstatus(AccountStatus.ACTIVE);
        user.setFailedLoginAttempts(0);
        user.setAccountLockedAt(null);
        userRepository.save(user);
        
        
        // Send email notification
        emailService.sendSimpleMessage(
            user.getUserEmail(),
            "Account Unlocked",
            "Dear " + user.getUserFullName() + ",\n\n" +
            "Your account has been unlocked by the administrator. You can now log in again.\n\n" +
            "Thank you."
        );
        return ResponseEntity.ok(Map.of("message", "Account unlocked."));
    }
    
    
    //Lock Account
    @PutMapping("/lock-account")
    public ResponseEntity<?> lockAccount(@RequestParam String email) {
        Optional<User> opt = userRepository.findByEmail(email);
        if (opt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not found."));
        }
        User user = opt.get();
      
 
        user.setAccountstatus(AccountStatus.ADMIN_LOCKED);
        user.setFailedLoginAttempts(0);
        user.setAccountLockedAt(LocalDateTime.now());
        userRepository.save(user);
        
        
        // Send email notification
        emailService.sendSimpleMessage(
            user.getUserEmail(),
            "Account Unlocked",
            "Dear " + user.getUserFullName() + ",\n\n" +
            "Your account has been unlocked by the administrator. You can now log in again.\n\n" +
            "Thank you."
        );
        return ResponseEntity.ok(Map.of("message", "Account locked."));
    }
    
    //View All suppliers
    @GetMapping("/getSupplier")
      public List<Supplier> getSuppliers(){
    	  return supplierrepo.findAll();
    	  
      }
 
   public static class ResetPasswordRequest {
    	
    	
    	
    	private String currentPassword;
    	
    	
    	private String newPassword;
    	
    	
        public String getCurrentPassword() {
			return currentPassword;
		}
		public void setCurrentPassword(String currentPassword) {
			this.currentPassword = currentPassword;
		}
		
        // Getter & Setter
        public String getNewPassword() {
            return newPassword;
        }
        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }
   
   @PutMapping("/{id}/profile")
   public ResponseEntity<User> updateProfile(@PathVariable String id,
                                             @RequestBody UserProfileUpdateDTO dto) {
       User updatedUser = userService.updateUserProfile(id, dto);
       return ResponseEntity.ok(updatedUser);
   }

 
 
}
 
 