package com.bookingapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.bookingapp.util.ErrorCode;

@ControllerAdvice
public class GenericExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorCode> handleException(BookingApplicationException e){

        return new ResponseEntity<>(e.getErrorCode(), e.getErrorCode().getHttpStatus());

    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(Exception e){

        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

    }


}
