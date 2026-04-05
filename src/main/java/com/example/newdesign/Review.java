package com.example.newdesign;

import java.time.LocalDateTime;

public class Review {
    private int id;
    private int reviewerId;   // User who wrote the review
    private int revieweeId;   // User being reviewed
    private int exchangeId;   // Which exchange this review is for
    private int rating;       // 1-5 stars
    private String comment;
    private LocalDateTime createdAt;

    // Default constructor
    public Review() {}

    // Constructor for new review
    public Review(int reviewerId, int revieweeId, int exchangeId, int rating, String comment) {
        this.reviewerId = reviewerId;
        this.revieweeId = revieweeId;
        this.exchangeId = exchangeId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
    }

    // Full constructor
    public Review(int id, int reviewerId, int revieweeId, int exchangeId,
                  int rating, String comment, LocalDateTime createdAt) {
        this.id = id;
        this.reviewerId = reviewerId;
        this.revieweeId = revieweeId;
        this.exchangeId = exchangeId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getReviewerId() { return reviewerId; }
    public void setReviewerId(int reviewerId) { this.reviewerId = reviewerId; }

    public int getRevieweeId() { return revieweeId; }
    public void setRevieweeId(int revieweeId) { this.revieweeId = revieweeId; }

    public int getExchangeId() { return exchangeId; }
    public void setExchangeId(int exchangeId) { this.exchangeId = exchangeId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isValidRating() {
        return rating >= 1 && rating <= 5;
    }

    @Override
    public String toString() {
        return rating + "★: " + (comment != null ? comment : "No comment");
    }
}