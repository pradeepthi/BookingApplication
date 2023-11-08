package com.bookingapp.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
public class User {

    private Long id;
    private String name;
    private String email;
    private UserStatus status;
    private LocalDate createdAt;
    private LocalDate lastUpdatedAt;
    private UserRole role;
}
