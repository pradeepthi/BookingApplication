package com.bookingapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.bookingapp.util.ErrorCode;

@ControllerAdvice
public class GenericExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<?> handleException(BookingApplicationException e){

        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e.getErrorCode());

    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(Exception e){

        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

    }


}
