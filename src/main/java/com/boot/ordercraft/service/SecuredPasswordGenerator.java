package com.boot.ordercraft.service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SecuredPasswordGenerator {

    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()-_+=<>?";

    // Combine all categories for filling remaining characters
    private static final String ALL = UPPERCASE + LOWERCASE + DIGITS + SPECIAL;

    private static final int PASSWORD_LENGTH = 12;

    @SuppressWarnings("unused")
	public static String generatePassword() {
        if (PASSWORD_LENGTH < 8) {
            throw new IllegalArgumentException("Password length must be at least 8.");
        }

        SecureRandom random = new SecureRandom();
        List<Character> passwordChars = new ArrayList<>();

        // Ensure one character from each category
        passwordChars.add(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        passwordChars.add(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        passwordChars.add(DIGITS.charAt(random.nextInt(DIGITS.length())));
        passwordChars.add(SPECIAL.charAt(random.nextInt(SPECIAL.length())));

        // Fill the rest
        for (int i = 4; i < PASSWORD_LENGTH; i++) {
            passwordChars.add(ALL.charAt(random.nextInt(ALL.length())));
        }

        // Shuffle so predictable order is avoided
        Collections.shuffle(passwordChars, random);

        // Build password string
        StringBuilder password = new StringBuilder();
        for (char c : passwordChars) {
            password.append(c);
        }

        return password.toString();
    }

    public static void main(String[] args) {
        System.out.println("Secure Password: " + generatePassword());
    }
}
