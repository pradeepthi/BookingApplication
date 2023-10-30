package com.bookingapp.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BookingStatus {
    AVAILABLE (1),
    RESERVED (2),
    CANCELED (3);

    private int statusCode;

}
