package com.bookingapp.util;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@RequiredArgsConstructor
@Getter
@ToString
public enum ErrorCode {

    INVALID_USER_ID(HttpStatus.BAD_REQUEST, "ERR_001", "Invalid user id"),
    INVALID_PROPERTY_ID(HttpStatus.BAD_REQUEST, "ERR_002", "Invalid property id"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "ERR_003", "Invalid request"),
    PROPERTY_UNAVAILABLE(HttpStatus.BAD_REQUEST, "ERR_004", "Property is unavailable"),
    INVALID_BOOKING_TYPE(HttpStatus.BAD_REQUEST, "ERR_004", "Invalid Booking Type"),
    INVALID_DATE_RANGE(HttpStatus.BAD_REQUEST, "ERR_005", "Invalid Date Range"),
    NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE, "ERR_006", "Unacceptable Role or Permission");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorDescription;

}
