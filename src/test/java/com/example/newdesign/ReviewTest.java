package com.example.newdesign;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

/**
 * Unit tests for the Review model class.
 * Tests constructors, getters/setters, rating validation, and string formatting.
 */
class ReviewTest {

    @Test
    void testFullConstructor() {
        LocalDateTime now = LocalDateTime.now();
        Review review = new Review(1, 2, 3, 100, 5, "Great exchange!", now);

        assertEquals(1, review.getId());
        assertEquals(2, review.getReviewerId());
        assertEquals(3, review.getRevieweeId());
        assertEquals(100, review.getExchangeId());
        assertEquals(5, review.getRating());
        assertEquals("Great exchange!", review.getComment());
        assertEquals(now, review.getCreatedAt());
    }

    @Test
    void testNewReviewConstructor() {
        Review review = new Review(5, 6, 200, 4, "Good session");

        assertEquals(5, review.getReviewerId());
        assertEquals(6, review.getRevieweeId());
        assertEquals(200, review.getExchangeId());
        assertEquals(4, review.getRating());
        assertEquals("Good session", review.getComment());
        assertNotNull(review.getCreatedAt());
    }

    @Test
    void testDefaultConstructorAndSetters() {
        Review review = new Review();
        review.setId(10);
        review.setReviewerId(7);
        review.setRevieweeId(8);
        review.setExchangeId(300);
        review.setRating(3);
        review.setComment("Okay experience");
        LocalDateTime testTime = LocalDateTime.of(2024, 1, 15, 10, 30);
        review.setCreatedAt(testTime);

        assertEquals(10, review.getId());
        assertEquals(7, review.getReviewerId());
        assertEquals(8, review.getRevieweeId());
        assertEquals(300, review.getExchangeId());
        assertEquals(3, review.getRating());
        assertEquals("Okay experience", review.getComment());
        assertEquals(testTime, review.getCreatedAt());
    }

    @Test
    void testIsValidRating_ValidRatings() {
        Review review = new Review();

        review.setRating(1);
        assertTrue(review.isValidRating());

        review.setRating(2);
        assertTrue(review.isValidRating());

        review.setRating(3);
        assertTrue(review.isValidRating());

        review.setRating(4);
        assertTrue(review.isValidRating());

        review.setRating(5);
        assertTrue(review.isValidRating());
    }

    @Test
    void testIsValidRating_InvalidRatings() {
        Review review = new Review();

        review.setRating(0);
        assertFalse(review.isValidRating());

        review.setRating(6);
        assertFalse(review.isValidRating());

        review.setRating(-1);
        assertFalse(review.isValidRating());

        review.setRating(10);
        assertFalse(review.isValidRating());
    }

    @Test
    void testToString() {
        Review review = new Review(1, 2, 100, 5, "Amazing teacher!");
        String result = review.toString();

        assertTrue(result.contains("5★"));
        assertTrue(result.contains("Amazing teacher!"));
    }

    @Test
    void testToStringWithNullComment() {
        Review review = new Review(1, 2, 100, 4, null);
        String result = review.toString();

        assertTrue(result.contains("4★"));
        assertTrue(result.contains("No comment"));
    }

    @Test
    void testToStringWithEmptyComment() {
        Review review = new Review(1, 2, 100, 3, "");
        String result = review.toString();

        assertTrue(result.contains("3★:"));
        assertEquals("3★: ", result);
    }
}