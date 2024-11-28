package com.akshay.book.repository;

import com.akshay.book.bean.Book.Book;

import org.springframework.data.jpa.repository.JpaRepository;


public interface BookRepository extends JpaRepository<Book, Integer> {

}
