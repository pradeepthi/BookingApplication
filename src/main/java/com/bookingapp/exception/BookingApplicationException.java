package com.bookingapp.exception;

import lombok.Getter;
import lombok.Setter;
import com.bookingapp.util.ErrorCode;

@Getter
@Setter
public class BookingApplicationException extends RuntimeException {

    private ErrorCode errorCode;

    public BookingApplicationException(ErrorCode errorCode){
        super(errorCode.getErrorCode());
        this.errorCode = errorCode;
    }
}
