//package com.example.myapplication;
//import org.junit.Before;
//import org.junit.Test;
//import static org.junit.Assert.*;
//
//import com.example.myapplication.models.User;
//
//public class UserUnitTest {
//    // declaring two  instances of the user class for testing
//    private User user1;
//    private User user2;
//
//
//    // Sets up the initial state by creating instances of User class
//    @Before
//    public void setUp() {
//        user1 = new User("John Doe", "password123");
//        user2 = new User("Jane Doe", "password456");
//    }
//
//    @Test
//    public void testSetUserName() {
//        user1.setUsername("John Doe");
//        user2.setUsername("Jane Doe");
//    }
//    @Test
//    public void testGetUserName() {
//        assertEquals("John Doe", user1.getUsername());
//        assertEquals("Jane Doe", user2.getUsername());
//    }
//
//    @Test
//    public void testSetPassword() {
//        user1.setPassword("password123");
//        user2.setPassword("password456");
//    }
//
//    @Test
//    public void testGetPassword() {
//        assertEquals("password123", user1.getPassword());
//        assertEquals("password456", user2.getPassword());
//    }
//}
