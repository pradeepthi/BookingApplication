package com.bookingapp.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    INVALID_USER_ID(HttpStatus.BAD_REQUEST, "ERR_001", "Invalid user id"),
    INVALID_PROPERTY_ID(HttpStatus.BAD_REQUEST, "ERR_002", "Invalid property id"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "ERR_003", "Invalid request"),
    PROPERTY_UNAVAILABLE(HttpStatus.BAD_REQUEST, "ERR_004", "Property is unavailable");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorDescription;

}
