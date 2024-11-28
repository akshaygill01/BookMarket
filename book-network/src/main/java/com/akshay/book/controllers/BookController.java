package com.akshay.book.controllers;

import com.akshay.book.bean.Book.BookRequest;
import com.akshay.book.models.Response;
import com.akshay.book.services.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @PostMapping("/save-book")
    public ResponseEntity<Response<String>> saveBook(@Valid @RequestBody BookRequest request , Authentication connectedUser) {
        try {
            log.trace("Book Create request: {}", request);
            return ResponseEntity.ok(bookService.saveBook(request , connectedUser));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Response.error(e.getMessage()));
        }
    }

}
