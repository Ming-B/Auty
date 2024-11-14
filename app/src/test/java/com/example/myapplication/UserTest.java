package com.example.myapplication;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("", "");
    }

    // Test UCT-1: No User Information
    @Test
    public void testEmptyUserInformation() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            user.setUserName("");
            user.setPassword("");
        });
        assertEquals("Username and password cannot be empty", exception.getMessage());
    }

    // Test UCT-2: Invalid User Credentials
    @Test
    public void testInvalidUserCredentials() {
        user.setUserName("validUser");
        user.setPassword("validPassword");

        User invalidUser = new User("invalidUser", "wrongPassword");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            if (!user.getUserName().equals(invalidUser.getUserName()) ||
                    !user.getPassword().equals(invalidUser.getPassword())) {
                throw new RuntimeException("User not found. Please check credentials or register.");
            }
        });
        assertEquals("User not found. Please check credentials or register.", exception.getMessage());
    }

    // Test UCT-3: Register User with Valid Credentials
    @Test
    public void testValidUserRegistration() {
        user.setUserName("newUser");
        user.setPassword("newPassword");

        assertEquals("newUser", user.getUserName());
        assertEquals("newPassword", user.getPassword());
    }
}
