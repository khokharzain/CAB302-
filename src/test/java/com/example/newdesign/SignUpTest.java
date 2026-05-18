package com.example.newdesign;


import com.example.newdesign.controller.signUpController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SignUpTest {

    // =============================
    // EMAIL TESTS
    // =============================

    @Test
    void testValidEmail() {

        signUpController controller = new signUpController();

        boolean result =
                controller.isValidEmail("amir@gmail.com");

        assertTrue(result);
    }

    @Test
    void testInvalidEmail() {

        signUpController controller = new signUpController();

        boolean result =
                controller.isValidEmail("amirgmail.com");

        assertFalse(result);
    }

    // =============================
    // PHONE TESTS
    // =============================

    @Test
    void testValidPhone() {

        signUpController controller = new signUpController();

        boolean result =
                controller.isValidPhone("0412345678");

        assertTrue(result);
    }

    @Test
    void testShortPhone() {

        signUpController controller = new signUpController();

        boolean result =
                controller.isValidPhone("123");

        assertFalse(result);
    }

    @Test
    void testPhoneWithLetters() {

        signUpController controller = new signUpController();

        boolean result =
                controller.isValidPhone("04abc56789");

        assertFalse(result);
    }

    // =============================
    // PASSWORD TESTS
    // =============================

    @Test
    void testValidPassword() {

        signUpController controller = new signUpController();

        boolean result =
                controller.isValidPassword("password123");

        assertTrue(result);
    }

    @Test
    void testShortPassword() {

        signUpController controller = new signUpController();

        boolean result =
                controller.isValidPassword("123");

        assertFalse(result);
    }

    // =============================
    // NAME TESTS
    // =============================

    @Test
    void testValidName() {

        signUpController controller = new signUpController();

        boolean result =
                controller.isValidName("Amir", "Amir");

        assertTrue(result);
    }

    @Test
    void testEmptyName() {

        signUpController controller = new signUpController();

        boolean result =
                controller.isValidName("", "");

        assertFalse(result);
    }
}