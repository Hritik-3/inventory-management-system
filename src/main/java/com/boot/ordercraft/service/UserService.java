package com.boot.ordercraft.service;
 
 
import com.boot.ordercraft.dto.UserProfileUpdateDTO;
import com.boot.ordercraft.model.AccountStatus;
import com.boot.ordercraft.model.Address;
import com.boot.ordercraft.model.Role;
import com.boot.ordercraft.model.User;
import com.boot.ordercraft.repository.AddressRepository;
import com.boot.ordercraft.repository.RoleRepository;
import com.boot.ordercraft.repository.UserRepository;
import com.boot.ordercraft.service.MailService.MailService;
 
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
 
@Service
public class UserService {
 
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
 
    @Autowired
    public UserService(UserRepository userRepository,
                       AddressRepository addressRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       MailService mailService) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService=mailService;
        
    }
 
public User registerUser(User user) {
    	
    	String generatedUserId = generateCustomUserId(user);
        user.setUserId(generatedUserId);
    	
        if (userRepository.existsByUserEmail(user.getUserEmail())) {
            throw new RuntimeException("Email already in use");
        }
 
        // Save address
        if (user.getAddress() != null) {
            Address savedAddress = addressRepository.save(user.getAddress());
            user.setAddress(savedAddress);
        }
 
        // Set role
        Role role = roleRepository.findById(user.getRole().getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRole(role);
 
        String plainPassword;
 
        // Handle password
        if (user.getUserPassword() == null || user.getUserPassword().isBlank()) {
            // Auto-generate temp password
            plainPassword = UUID.randomUUID().toString().substring(0, 8);
            user.setUserPassword(passwordEncoder.encode(plainPassword));
        } else {
            // Use provided password
            plainPassword = user.getUserPassword(); // Save plain version to send in email
            user.setUserPassword(passwordEncoder.encode(plainPassword));
        }
        
        user.setAccountstatus(AccountStatus.ACTIVE);
        user.setMustReset(true);
        
     // Normalize email before saving
        user.setUserEmail(user.getUserEmail().trim().toLowerCase());
 
        // Save user
        User savedUser = userRepository.save(user);
        
        
 
 
        // Send email with credentials
        mailService.sendEmail(
            user.getUserEmail(),
            "Welcome to Order Craft - Your Login Credentials",
            "Hello " + user.getUserFullName() + ",\n\n" +
            "Your account has been created.\n\n" +
            "Login Username: " + user.getUserEmail() + "\n" +
            "Password: " + plainPassword + "\n\n" +
            "Please login and reset your password.\n\n" +
            "Regards,\nOrder Craft Admin Team"
        );
 
        return savedUser;
    }
 
 
public Optional<User> searchByEmailOrName(String query) {
    return userRepository.findByEmail( query);
}
 
 
private String generateCustomUserId(User user) {
    // 1. Get role prefix
    String rolePrefix = switch (user.getRole().getRoleName()) {
        case "PROCUREMENT_OFFICER" -> "PO";
        case "INVENTORY_MANAGER" -> "IM";
        case "ADMIN" -> "AD";
        case "PRODUCTION_MANAGER" -> "PM";
        case "PRODUCTION_SCHEDULER" -> "PS";
        case "SUPPLIER" -> "SUPP";
        default -> "XX";
    };
 
    // 2. Get city code (first 3 letters of city)
    String cityCode = Optional.ofNullable(user.getAddress())
        .map(a -> a.getAddressCity())
        .filter(city -> city.length() >= 3)
        .map(city -> city.substring(0, 3).toUpperCase())
        .orElse("LOC");
 
    // 3. Generate a random 4-digit number
    int randomNumber = new Random().nextInt(9000) + 1000;
 
    return rolePrefix + cityCode + randomNumber;
}
 
    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
 
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }
 
    public User updateUser(String id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
 
        existingUser.setUserFullName(updatedUser.getUserFullName());
        existingUser.setUserEmail(updatedUser.getUserEmail());
        existingUser.setUserMobile(updatedUser.getUserMobile());
        existingUser.setUserProfileImg(updatedUser.getUserProfileImg());
 
        if (updatedUser.getUserPassword() != null && !updatedUser.getUserPassword().isBlank()) {
            existingUser.setUserPassword(passwordEncoder.encode(updatedUser.getUserPassword()));
        }
 
        if (updatedUser.getAddress() != null) {
            Address savedAddress = addressRepository.save(updatedUser.getAddress());
            existingUser.setAddress(savedAddress);
        }
 
        if (updatedUser.getRole() != null) {
            Role role = roleRepository.findById(updatedUser.getRole().getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            existingUser.setRole(role);
        }
 
        return userRepository.save(existingUser);
    }
    
    
    
    
   
 
    
    
    
 
	public Optional<User> getByEmail(String username) {
		return userRepository.findByEmail(username);
	}
 
	public void saveUser(User user) {
		userRepository.save(user);
		
	}
 
	public Optional<User> getUserByUsername(String username) {
	    return userRepository.findByUserName(username);
	}
	
	
	public void lockUserByAdmin(String userId) {
	    Optional<User> optionalUser = userRepository.findById(userId);
	    if (optionalUser.isEmpty()) {
	        throw new RuntimeException("User not found with ID: " + userId);
	    }
 
	    User user = optionalUser.get();
	    user.setAccountstatus(AccountStatus.ADMIN_LOCKED);
	    userRepository.save(user);
	}
 
//	public void toggleAccountStatus(String email) {
//	    User user = userRepository.findByEmail(email)
//	        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//	    if (user.getAccountstatus() == AccountStatus.LOCKED) {
//	        user.setAccountstatus(AccountStatus.ACTIVE);
//	    } else if (user.getAccountstatus() == AccountStatus.ACTIVE) {
//	        user.setAccountstatus(AccountStatus.LOCKED);
//	    } else {
//	        throw new IllegalStateException("Cannot toggle status for INACTIVE users.");
//	    }
//
//	    userRepository.save(user);
//	}
 
	
	public boolean toggleAccountStatus(String email, String action) {
	    Optional<User> userOpt = userRepository.findByEmail(email);
	    if (userOpt.isEmpty()) return false;
 
	    User user = userOpt.get();
 
	    if ("lock".equalsIgnoreCase(action)) {
	        user.setAccountstatus(AccountStatus.LOCKED);
	    } else if ("unlock".equalsIgnoreCase(action)) {
	        user.setAccountstatus(AccountStatus.ACTIVE);
	    } else {
	        return false;
	    }
 
	    userRepository.save(user);
	    return true;
	}
 
	
	public void lockAccount(String email) {
	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));
	    user.setAccountstatus(AccountStatus.LOCKED);
	    userRepository.save(user);
	}
 
	public void unlockAccount(String email) {
	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));
	    user.setAccountstatus(AccountStatus.ACTIVE);
	    userRepository.save(user);
	}
	
	public void unlockUserByAdmin(String userId) {
	    Optional<User> optionalUser = userRepository.findById(userId);
	    if (optionalUser.isEmpty()) {
	        throw new RuntimeException("User not found with ID: " + userId);
	    }
 
	    User user = optionalUser.get();
	    user.setAccountstatus(AccountStatus.ACTIVE);
	    userRepository.save(user);
	}
 
	
	public void deactivateUser(String id) {
	    Optional<User> optionalUser = userRepository.findById(id);
 
	    if (optionalUser.isPresent()) {
	        User user = optionalUser.get();
	        user.setAccountstatus(AccountStatus.INACTIVE);  // or `user.setActive(false);`
	        userRepository.save(user);
	    } else {
	        throw new RuntimeException("User not found with ID: " + id);
	    }
	}
 
	
	public List<User> getAllUsers() {
	    return userRepository.findAll();
	}
 
	
	
	public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
 
    public void updatePassword(String email, String newPassword) {
        User user = findByEmail(email);
        user.setUserPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
 
    
 // Fetch all locked accounts
    public List<User> getLockedAccounts() {
        return userRepository.findAll().stream()
                .filter(user -> user.getAccountstatus() != null &&
                        (user.getAccountstatus().name().contains("LOCKED")))
                .toList();
    }

    // Unlock all locked accounts
    public int unlockAllLockedAccounts() {
        List<User> lockedUsers = getLockedAccounts();

        lockedUsers.forEach(user -> {
            user.setAccountstatus(AccountStatus.ACTIVE);
            user.setFailedLoginAttempts(0);
            user.setAccountLockedAt(null);
        });

        userRepository.saveAll(lockedUsers);
        return lockedUsers.size();
    }
    
    public User updateUserProfile(String userId, UserProfileUpdateDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (dto.getUserFullName() != null) {
            user.setUserFullName(dto.getUserFullName());
        }

        if (dto.getUserMobile() != null) {
            user.setUserMobile(dto.getUserMobile());
        }

        if (dto.getUserProfileImg() != null) {
            user.setUserProfileImg(dto.getUserProfileImg());
        }

        // Update Address (create new if null)
        Address address = user.getAddress();
        if (address == null) {
            address = new Address();
        }

        if (dto.getAddressStreet() != null) address.setAddressStreet(dto.getAddressStreet());
        if (dto.getAddressCity() != null) address.setAddressCity(dto.getAddressCity());
        if (dto.getAddressState() != null) address.setAddressState(dto.getAddressState());
        if (dto.getAddressPostalCode() != null) address.setAddressPostalCode(dto.getAddressPostalCode());
        if (dto.getAddressCountry() != null) address.setAddressCountry(dto.getAddressCountry());

        user.setAddress(address);

        return userRepository.save(user);
    }


 
	
}
 