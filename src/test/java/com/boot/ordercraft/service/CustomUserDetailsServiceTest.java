package com.boot.ordercraft.service;
 
import com.boot.ordercraft.model.Role;
import com.boot.ordercraft.model.User;
import com.boot.ordercraft.repository.UserRepository;
import com.boot.ordercraft.service.CustomUserDetailsService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
 
import java.util.Optional;
 
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
 
@ExtendWith(SpringExtension.class)
class CustomUserDetailsServiceTest {
 
    @Mock
    private UserRepository userRepository;
 
    @InjectMocks
    private CustomUserDetailsService userDetailsService;
 
    @Test
    void testLoadUserByUsername_Success() {
        // Given
        Role role = new Role();
        role.setRoleName("Admin");
 
        User user = new User();
        user.setUserEmail("admin@example.com");
        user.setUserPassword("securepass");
        user.setRole(role);
 
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(user));
 
        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("admin@example.com");
 
        // Then
        assertNotNull(userDetails);
        assertEquals("admin@example.com", userDetails.getUsername());
        assertEquals("securepass", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                   .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }
 
    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Given
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());
 
        // Then
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("missing@example.com");
        });
    }
}
 
 