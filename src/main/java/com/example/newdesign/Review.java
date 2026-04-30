package com.example.newdesign;

import java.time.LocalDateTime;

/**
 * A simple model class representing a review with reviewerId, revieweeId, exchangeId, rating, comment and createdAt.
 */
public class Review {
    private int id;
    private int reviewerId;
    private int revieweeId;
    private int exchangeId;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;

    public Review() {}

    /**
     * @param reviewerId
     * @param revieweeId
     * @param exchangeId
     * @param rating
     * @param comment
     */
    public Review(int reviewerId, int revieweeId, int exchangeId, int rating, String comment) {
        this.reviewerId = reviewerId;
        this.revieweeId = revieweeId;
        this.exchangeId = exchangeId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * @param id
     * @param reviewerId
     * @param revieweeId
     * @param exchangeId
     * @param rating
     * @param comment
     * @param createdAt
     */
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

    /**
     * @return id
     */
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    /**
     * @return reviewerId
     */
    public int getReviewerId() { return reviewerId; }
    public void setReviewerId(int reviewerId) { this.reviewerId = reviewerId; }

    /**
     * @return revieweeId
     */
    public int getRevieweeId() { return revieweeId; }
    public void setRevieweeId(int revieweeId) { this.revieweeId = revieweeId; }

    /**
     * @return exchangeId
     */
    public int getExchangeId() { return exchangeId; }
    public void setExchangeId(int exchangeId) { this.exchangeId = exchangeId; }

    /**
     * @return rating
     */
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    /**
     * @return comment
     */
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    /**
     * @return createdAt
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    /**
     * @return true or false
     */
    public boolean isValidRating() {
        return rating >= 1 && rating <= 5;
    }

    @Override
    public String toString() {
        return rating + "★: " + (comment != null ? comment : "No comment");
    }
}