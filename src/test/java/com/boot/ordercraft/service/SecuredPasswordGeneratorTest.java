package com.boot.ordercraft.service;
 
import org.junit.jupiter.api.Test;

import com.boot.ordercraft.service.SecuredPasswordGenerator;

import static org.junit.jupiter.api.Assertions.*;
 
class SecuredPasswordGeneratorTest {
 
    @Test
    void testGeneratedPasswordIsNotNull() {
        String password = SecuredPasswordGenerator.generatePassword();
        assertNotNull(password, "Password should not be null");
    }
 
    @Test
    void testGeneratedPasswordLength() {
        String password = SecuredPasswordGenerator.generatePassword();
        assertEquals(12, password.length(), "Password should be 12 characters long");
    }
 
    @Test
    void testPasswordContainsUppercase() {
        String password = SecuredPasswordGenerator.generatePassword();
        assertTrue(password.matches(".*[A-Z].*"), "Password should contain at least one uppercase letter");
    }
 
    @Test
    void testPasswordContainsLowercase() {
        String password = SecuredPasswordGenerator.generatePassword();
        assertTrue(password.matches(".*[a-z].*"), "Password should contain at least one lowercase letter");
    }
 
    @Test
    void testPasswordContainsDigit() {
        String password = SecuredPasswordGenerator.generatePassword();
        assertTrue(password.matches(".*[0-9].*"), "Password should contain at least one digit");
    }
 
    @Test
    void testPasswordContainsSpecialCharacter() {
        String password = SecuredPasswordGenerator.generatePassword();
        assertTrue(password.matches(".*[!@#$%^&*()\\-_=+<>?].*"), "Password should contain at least one special character");
    }
 
    @Test
    void testPasswordIsRandom() {
        String password1 = SecuredPasswordGenerator.generatePassword();
        String password2 = SecuredPasswordGenerator.generatePassword();
        assertNotEquals(password1, password2, "Passwords should be different each time");
    }
}
 
 