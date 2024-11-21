package com.akshay.book.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Response<T> {
    private  boolean success;
    private HttpStatus httpStatus;
    private   String message;
    private Set<String> validationErrors;
    private   T data;

    public static <T> Response<T> success(String message, T data) {
        return new Response<>(true,HttpStatus.OK, message, data);
    }


    public  static <T> Response<T> error(String message) {
        return new Response<>(false,HttpStatus.INTERNAL_SERVER_ERROR , message ,null);
    }

    public  static <T> Response<T> error(String message,HttpStatus statusCode) {
        return new Response<>(false,statusCode , message ,null);
    }


}
