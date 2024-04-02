package com.luv2code.springbootlibrary.requestmodels;

import lombok.Data;

import java.util.Optional;

@Data
public class ReviewRequest {

    private double rating;

    private Long bookId;

    private Optional<String> reviewDescription;

    public Long getBookId() {
        return bookId;
    }

    public double getRating() {
        return rating;
    }

    public Optional<String> getReviewDescription() {
        return reviewDescription;
    }
}