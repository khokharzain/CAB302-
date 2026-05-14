package com.example.newdesign;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Hobby model class.
 * Tests constructors, getters/setters, and string formatting.
 */
class HobbyTest {

    @Test
    void testBasicConstructor() {
        Hobby hobby = new Hobby(1, "Chess");

        assertEquals(1, hobby.getUserId());
        assertEquals("Chess", hobby.getHobbyName());
        assertEquals(0, hobby.getId());
    }

    @Test
    void testFullConstructor() {
        Hobby hobby = new Hobby(10, 5, "Hiking");

        assertEquals(10, hobby.getId());
        assertEquals(5, hobby.getUserId());
        assertEquals("Hiking", hobby.getHobbyName());
    }

    @Test
    void testDefaultConstructorAndSetters() {
        Hobby hobby = new Hobby();
        hobby.setId(20);
        hobby.setUserId(3);
        hobby.setHobbyName("Reading");

        assertEquals(20, hobby.getId());
        assertEquals(3, hobby.getUserId());
        assertEquals("Reading", hobby.getHobbyName());
    }

    @Test
    void testToString() {
        Hobby hobby = new Hobby(1, "Gaming");
        assertEquals("Gaming", hobby.toString());
    }
}