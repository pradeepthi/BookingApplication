package com.bookingapp.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserRole {

    GUEST(1),
    PROPERTY_OWNER(2),
    PROPERTY_MANAGER(3);

    private int roleId;
}
