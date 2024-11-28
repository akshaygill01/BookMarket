package com.akshay.book.bean.Book;

import jakarta.persistence.criteria.CriteriaBuilder;

public record BookRequest (
        String title,
        String authorName,
        String isbn,
        String synopsis,
        boolean shareable
) {

}

