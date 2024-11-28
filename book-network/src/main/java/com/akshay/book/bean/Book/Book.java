package com.akshay.book.bean.Book;

import com.akshay.book.bean.Feedback;
import com.akshay.book.bean.User;
import com.akshay.book.common.BaseEntity;
import com.akshay.book.history.BookTransactionHistory;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "books")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Book extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 100)
    private String authorName;

    @Column(length = 500)
    private String description;

    @Column(unique = true, nullable = false)
    private String identifier;

    private String coverImage;

    private Boolean archived;

    private Boolean shareable;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany( mappedBy = "book")
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> histories;


}
