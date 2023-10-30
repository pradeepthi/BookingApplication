package com.bookingapp.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class User {

    private Long id;
    private String name;
    private String email;
    private UserStatus status;
    private Date createdAt;
    private Date lastUpdatedAt;
    private UserRole role;
}
