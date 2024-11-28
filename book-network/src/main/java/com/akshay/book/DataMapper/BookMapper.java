package com.akshay.book.DataMapper;

import com.akshay.book.bean.Book.Book;
import com.akshay.book.bean.Book.BookRequest;

public class BookMapper {

    public Book toBook(BookRequest request) {
       return  Book.builder()
                .title(request.title())
                .description(request.synopsis())
                .authorName(request.authorName())
               .archived(false)
                .shareable(request.shareable())
                .build();
    }

}
