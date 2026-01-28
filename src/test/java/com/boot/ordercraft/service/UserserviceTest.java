package com.boot.ordercraft.service;

import com.boot.ordercraft.model.*;
import com.boot.ordercraft.repository.AddressRepository;
import com.boot.ordercraft.repository.RoleRepository;
import com.boot.ordercraft.repository.UserRepository;
import com.boot.ordercraft.service.UserService;
import com.boot.ordercraft.service.MailService.MailService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserserviceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MailService mailService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_withValidData() {
        User user = new User();
        user.setUserEmail("test@example.com");
        user.setUserPassword("password");
        user.setUserFullName("Test User");
        Address address = new Address();
        address.setAddressCity("Hyderabad");
        user.setAddress(address);
        Role role = new Role();
        role.setRoleId(1L);
        role.setRoleName("ADMIN");
        user.setRole(role);

        when(userRepository.existsByUserEmail(anyString())).thenReturn(false);
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(addressRepository.save(any())).thenReturn(address);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenReturn(user);

        User result = userService.registerUser(user);
        assertEquals("test@example.com", result.getUserEmail());
        verify(mailService, times(1)).sendEmail(any(), any(), contains("Password:"));
    }

    @Test
    void testSearchByEmailOrName_returnsUser() {
        User user = new User();
        user.setUserEmail("abc@example.com");
        when(userRepository.findByEmail("abc@example.com")).thenReturn(Optional.of(user));

        Optional<User> result = userService.searchByEmailOrName("abc@example.com");
        assertTrue(result.isPresent());
        assertEquals("abc@example.com", result.get().getUserEmail());
    }

    @Test
    void testGetUserById_found() {
        User user = new User();
        user.setUserId("USER123");
        when(userRepository.findById("USER123")).thenReturn(Optional.of(user));

        User result = userService.getUserById("USER123");
        assertEquals("USER123", result.getUserId());
    }

    @Test
    void testDeleteUser_success() {
        when(userRepository.existsById("USER123")).thenReturn(true);
        doNothing().when(userRepository).deleteById("USER123");

        assertDoesNotThrow(() -> userService.deleteUser("USER123"));
    }

    @Test
    void testUpdateUser_success() {
        User oldUser = new User();
        oldUser.setUserId("USER1");
        Role role = new Role();
        role.setRoleId(1L);
        role.setRoleName("ADMIN");
        Address address = new Address();
        address.setAddressCity("Hyd");

        User updated = new User();
        updated.setUserFullName("Updated Name");
        updated.setUserEmail("new@example.com");
        updated.setUserPassword("newpass");
        updated.setUserMobile("1234567890");
        updated.setRole(role);
        updated.setAddress(address);

        when(userRepository.findById("USER1")).thenReturn(Optional.of(oldUser));
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(addressRepository.save(any())).thenReturn(address);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any())).thenReturn(updated);

        User result = userService.updateUser("USER1", updated);
        assertEquals("Updated Name", result.getUserFullName());
        assertEquals("new@example.com", result.getUserEmail());
    }

    @Test
    void testLockUserByAdmin_success() {
        User user = new User();
        user.setUserId("USER1");

        when(userRepository.findById("USER1")).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        userService.lockUserByAdmin("USER1");
        assertEquals(AccountStatus.ADMIN_LOCKED, user.getAccountstatus());
    }

    @Test
    void testUnlockUserByAdmin_success() {
        User user = new User();
        user.setUserId("USER1");

        when(userRepository.findById("USER1")).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        userService.unlockUserByAdmin("USER1");
        assertEquals(AccountStatus.ACTIVE, user.getAccountstatus());
    }

    @Test
    void testDeactivateUser_success() {
        User user = new User();
        user.setUserId("USER1");

        when(userRepository.findById("USER1")).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        userService.deactivateUser("USER1");
        assertEquals(AccountStatus.INACTIVE, user.getAccountstatus());
    }

    @Test
    void testGetAllUsers_success() {
        List<User> users = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();
        assertEquals(2, result.size());
    }

    @Test
    void testFindByEmail_success() {
        User user = new User();
        user.setUserEmail("test@test.com");

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        User result = userService.findByEmail("test@test.com");
        assertEquals("test@test.com", result.getUserEmail());
    }

    @Test
    void testUpdatePassword_success() {
        User user = new User();
        user.setUserEmail("abc@abc.com");

        when(userRepository.findByEmail("abc@abc.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newpass")).thenReturn("encoded");
        when(userRepository.save(any())).thenReturn(user);

        userService.updatePassword("abc@abc.com", "newpass");
        assertEquals("encoded", user.getUserPassword());
    }

    @Test
    void testSaveUser_success() {
        User user = new User();
        user.setUserId("ID123");
        userService.saveUser(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetByEmail_found() {
        User user = new User();
        user.setUserEmail("xyz@test.com");
        when(userRepository.findByEmail("xyz@test.com")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getByEmail("xyz@test.com");
        assertTrue(result.isPresent());
    }

    @Test
    void testGetUserByUsername_found() {
        User user = new User();
        user.setUserName("uname");
        when(userRepository.findByUserName("uname")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByUsername("uname");
        assertTrue(result.isPresent());
    }
}
