package com.akshay.book.bean;

import com.akshay.book.bean.Book.Book;
import com.akshay.book.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Feedback extends BaseEntity {
    private Double rating;
    private String comment;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
}
