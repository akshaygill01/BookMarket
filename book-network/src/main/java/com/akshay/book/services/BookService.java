package com.akshay.book.services;

import com.akshay.book.DataMapper.BookMapper;
import com.akshay.book.bean.Book.Book;
import com.akshay.book.bean.Book.BookRequest;
import com.akshay.book.bean.User;
import com.akshay.book.models.Response;
import com.akshay.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookMapper bookMapper;
    private final BookRepository  bookRepository;


    public Response<String> saveBook(BookRequest request , Authentication connectedUser) throws Exception{
        User user = (User) connectedUser.getPrincipal();
        Book book  = bookMapper.toBook(request);

        book.setOwner(user);

        bookRepository.save(book);

        return Response.success("Book saved successfully");
    }
}
