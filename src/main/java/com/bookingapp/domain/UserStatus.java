package com.bookingapp.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserStatus {

    ACTIVE (1),
    INACTIVE(2);
    private int statusCode;
}
