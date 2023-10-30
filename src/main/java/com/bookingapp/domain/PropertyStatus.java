package com.bookingapp.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PropertyStatus {

    OPEN (1),
    BLOCKED (2),
    DELETED (3);

    private int statusCode;
}
