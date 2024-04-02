package com.luv2code.springbootlibrary.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Optional;

@Entity
@Table(name = "review")
@Data
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "date")
    @CreationTimestamp
    private Date date;

    @Column(name = "rating")
    private double rating;

    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "review_description")
    private String reviewDescription;

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setReviewDescription(String reviewDescription) {
        this.reviewDescription = reviewDescription;
    }

    public void setDate(java.sql.Date date) {
        this.date = date;
    }
}
