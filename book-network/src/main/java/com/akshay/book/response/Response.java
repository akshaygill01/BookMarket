package com.akshay.book.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class Response<T> {
    private final HttpStatus status;
    private final  String message;
    private final  T data;

    public static <T> Response<T> success(String message, T data) {
        return new Response<>(HttpStatus.OK, message, data);
    }


    public  static <T> Response<T> error(String message) {
        return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR , message ,null);
    }

    public  static <T> Response<T> error(String message,HttpStatus statusCode) {
        return new Response<>(statusCode , message ,null);
    }


}
