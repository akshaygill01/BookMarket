package com.akshay.book.handler;

import com.akshay.book.response.Response;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException( Exception e) {
        log.error("global exception: {}",e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Response.error(e.getMessage()));
    }


}
